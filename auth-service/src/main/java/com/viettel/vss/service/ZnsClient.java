package com.viettel.vss.service;

import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.ZnsSendRequest;
import com.viettel.vss.dto.ZnsSendResponse;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.util.MessageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Service
public class ZnsClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${zns.zalo}")
    String url;

    @Value("${zns.key}")
    String key;

    @Autowired
    MessageCommon messageCommon;

    public ZnsSendResponse sendZnsOtp(String otp, String phoneNumber) {
        // Body
        ZnsSendRequest req = new ZnsSendRequest();
        req.setMode("unknown");
        req.setOaId("3161153324846370670");
        req.setPhone(phoneNumber);
        req.setSendingMode("unknown");
        req.setTemplateId(474354L);
        req.setTemplateData(Map.of("otp", otp));

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(key);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ZnsSendRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<ZnsSendResponse> resp = null;
        ZnsSendResponse znsSendResponse = new ZnsSendResponse();
        try {
          resp = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                  ZnsSendResponse.class
            );
            znsSendResponse= resp.getBody();
            znsSendResponse.setStatusCode(resp.getStatusCode().toString());
        } catch (HttpServerErrorException | HttpClientErrorException e ) {
            System.out.println("4xx error: " + e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            znsSendResponse.setStatus("FAILED");
            znsSendResponse.setStatusCode(e.getStatusCode().toString());
            znsSendResponse.setErrorMessage(e.getResponseBodyAsString());
        }

        return znsSendResponse;
    }
}
