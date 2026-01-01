package com.viettel.vss.constant.common;

import com.google.gson.Gson;
import com.viettel.vss.dto.EmailSenderRequest;
import com.viettel.vss.dto.TokenKcResponse;
import com.viettel.vss.repository.UserRepository;
import com.viettel.vss.util.CustomizeMessageCommon;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;


@Component
public class NotifySendMail {
    @Value("${notify.sendEmail.otpTemplate}")
    private String otpTemplate = "TEST-02";

    @Value("${keycloak.auth-server-url}")
    private String urlKC;

    @Value("${keycloak.realm}")
    private String realmKC;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${notify.sendMail.url}")
    private String sendEmailUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomizeMessageCommon customizeMessageCommon;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Hauvt4
     * Hàm gửi email template chung
     */
    public void sendEmailTemplate(String templateCode,
                                  String emailReceiver,
                                  String companyCode,
                                  Map<String, Object> params) {

        EmailSenderRequest emailSenderRequest = new EmailSenderRequest();
        emailSenderRequest.setEmailReceivers(emailReceiver);
        emailSenderRequest.setTemplateCode(templateCode);
        emailSenderRequest.setCompanyCode(companyCode);

        // Tạo JSON string cho params
        String objectsJson = new Gson().toJson(params);
        emailSenderRequest.setObjects(objectsJson);

        // Gửi request
        HttpHeaders headers = buildHeaders();
        HttpEntity<EmailSenderRequest> request = new HttpEntity<>(emailSenderRequest, headers);

        restTemplate.exchange(sendEmailUrl, HttpMethod.POST, request, String.class);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add("Authorization", getToken());
        return headers;
    }

    private String getToken() {
        String url = urlKC + "/realms/" + realmKC + "/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(OAuth2Constants.CLIENT_ID, clientId);
        map.add(OAuth2Constants.CLIENT_SECRET, clientSecret);
        map.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.CLIENT_CREDENTIALS);

        HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(map, headers);
        ResponseEntity<TokenKcResponse> httpResponse = restTemplate.postForEntity(url, httpRequest, TokenKcResponse.class);

        TokenKcResponse tokenResponse = httpResponse.getBody();
        return tokenResponse.getToken_type() + " " + tokenResponse.getAccess_token();
    }

}
