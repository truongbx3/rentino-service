package com.viettel.vss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.CheckDeviceOpenAPI;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.util.MessageCommon;
import com.viettel.vss.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAIService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String apiUrl;

    @Autowired
    private MessageCommon messageCommon;

    private final RestTemplate restTemplate = new RestTemplate();

    public CheckDeviceOpenAPI analyze(List<String> lstImage) throws Exception {

        if (lstImage.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.SCREENCHECK_NOT_ENOUGHT,
                    messageCommon.getMessage(BusinessExceptionCode.SCREENCHECK_NOT_ENOUGHT));
        }

        // Build content list for OpenAI
        List<Map<String, Object>> content = new ArrayList<>();

        // Add prompt text
        content.add(Map.of(
                "type", "text",
                "text", "Bạn là chuyên gia kiểm định ngoại hình điện thoại.\n" +
                        "Tôi sẽ gửi 6 ảnh của điện thoại Xiaomi 14:\n" +
                        "Ảnh mặt trước (kính/màn hình)\n" +
                        "Ảnh mặt sau (kính lưng)\n" +
                        "Yêu cầu chung\n" +
                        "Chỉ đánh giá dựa trên những gì nhìn thấy rõ trong ảnh.\n" +
                        "Nếu ảnh thiếu / mờ / out nét / chói phản quang che mất chi tiết: ưu tiên trả về Loai_0.\n" +
                        "Với mỗi mặt (trước/sau), hãy chọn đúng 1 loại duy nhất theo mức lỗi nghiêm trọng nhất quan sát được (ưu tiên theo thứ tự: Loai_5 > Loai_4 > Loai_3 > Loai_2 > Loai_1 > Loai_0).\n" +
                        "Khi đếm vết trầy: chỉ tính các vết trầy nhìn thấy rõ, không tính bụi/ vân tay/ phản xạ ánh sáng.\n" +
                        "\n" +
                        "1) Chấm mặt kính trước (màn hình)\n" +
                        "Trả về:\n" +
                        "Loai_1 nếu:\n" +
                        "Không thấy trầy xước hoặc nứt vỡ.\n" +
                        "Loai_2 nếu:\n" +
                        "Có 1–3 vết trầy nhẹ (hairline), không ảnh hưởng nhiều.\n" +
                        "Loai_3 nếu:\n" +
                        "Có hơn 3 vết trầy.\n" +
                        "Loai_4 nếu:\n" +
                        "Có nứt/vỡ nhẹ (nứt nhỏ, sứt nhẹ, chân chim nhẹ), nhưng không thuộc Loai_5.\n" +
                        "Loai_5 nếu có ít nhất 1 trong các lỗi sau:\n" +
                        "Không có ảnh, hoặc ảnh bị mờ/out nét/không đủ sáng khiến không thể kết luận.\n" +
                        "Nứt/vỡ nặng\n" +
                        "Viền màn hình bị ám màu khác thường (xanh/đỏ/…) → phân tích chênh màu từ tâm ra viền\n" +
                        "Có vết loang đen/tím → nhận diện vùng đen lan bất thường (đặc biệt trên nền sáng)\n" +
                        "Có bóng mờ/ghosting (hình lưu)\n" +
                        "Có sọc màn hình dọc/ngang\n" +
                        "Có điểm chết hoặc vùng không phát sáng\n" +
                        "\n" +
                        "2) Chấm mặt kính sau\n" +
                        "Trả về:\n" +
                        "Loai_1 nếu:\n" +
                        "Không thấy trầy xước hoặc nứt vỡ.\n" +
                        "Loai_2 nếu:\n" +
                        "Có 1–3 vết trầy nhẹ.\n" +
                        "Loai_3 nếu:\n" +
                        "Có hơn 3 vết trầy.\n" +
                        "Loai_4 nếu:\n" +
                        "Có nứt/vỡ nhẹ.\n" +
                        "Loai_5 nếu:\n" +
                        "Có nứt/vỡ nặng.\n" +
                        "Không có ảnh, hoặc ảnh mờ/out nét, hoặc đang có ốp lưng che mặt sau.\n" +
                        "Output bắt buộc (chỉ trả JSON, không thêm chữ ngoài)\n" +
                        "{\n" +
                        "\"mat_truoc\": \"Loai_X\",\n" +
                        "\"mat_sau\": \"Loai_Y\",\n" +
                        "\"tong_ket\": \"Đánh giá ngắn gọn 1-2 câu, nêu lý do chính dẫn đến loại cao nhất; nếu Loai_0 thì nói rõ thiếu/mờ/ốp lưng.\"\n" +
                        "}"
        ));

        for (String image: lstImage){
            String base64 = imageUrlToBase64(image);
            if (base64!=null){
                content.add(Map.of(
                        "type", "image_url",
                        "image_url", Map.of("url", "data:image/png;base64," + base64)
                ));
            }
        }

        // Build final payload
        Map<String, Object> payload = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", content
                        )
                ),
                "temperature", 0.7,
                "response_format", Map.of("type", "json_object")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
//        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);

        ResponseEntity<String> res = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                req,
                String.class
        );

        CheckDeviceOpenAPI checkDeviceOpenAPI = null;

        if (res.getStatusCode().is2xxSuccessful()){
             checkDeviceOpenAPI = getContent(res);
        }
        return checkDeviceOpenAPI;
    }


    public static String imageUrlToBase64(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);

        }
    }
    public static void main(String[] args) throws IOException {
//        String originalUrl = "http://72.61.120.252:9000/rentino/mt_1765894714989.jpg";
        String originalUrl = "http://72.61.120.252:9000/rentino/ms_1765894715016.jpg";
        String base64EncodedUrl = imageUrlToBase64(originalUrl);
        System.out.println("Base64 Encoded URL: " + base64EncodedUrl);
    }
    public CheckDeviceOpenAPI getContent(ResponseEntity<String> res ) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(res.getBody());

        // Lấy content
        String content = root
                .path("choices")
                .get(0)
                .path("message")
                .path("content").asText();


        String value = extractJson(content);
        System.out.println("Content: " + value);

        return mapper.readValue(value, CheckDeviceOpenAPI.class);
    }
    public String extractJson(String input) {
        Pattern pattern = Pattern.compile("(?s)\\{.*?\\}");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

}

