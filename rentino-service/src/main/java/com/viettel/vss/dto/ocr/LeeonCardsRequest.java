package com.viettel.vss.dto.ocr;
import com.fasterxml.jackson.annotation.JsonProperty;
public class LeeonCardsRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("token")
    private String token;

    @JsonProperty("ImgFront")
    private String imgFront;

    @JsonProperty("ImgBack")
    private String imgBack;

    public LeeonCardsRequest() {}

    public LeeonCardsRequest(String username, String token, String imgFront, String imgBack) {
        this.username = username;
        this.token = token;
        this.imgFront = imgFront;
        this.imgBack = imgBack;
    }

    public String getUsername() { return username; }
    public String getToken() { return token; }
    public String getImgFront() { return imgFront; }
    public String getImgBack() { return imgBack; }

    public void setUsername(String username) { this.username = username; }
    public void setToken(String token) { this.token = token; }
    public void setImgFront(String imgFront) { this.imgFront = imgFront; }
    public void setImgBack(String imgBack) { this.imgBack = imgBack; }
}
