package com.viettel.vss.config.sercurity;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final Logger _logger = LoggerFactory.getLogger(JwtTokenProvider.class);



    // Tạo ra jwt từ thông tin user
    public String generateToken(String jwt, Map<String, Object> claims, long JWT_EXPIRATION, String JWT_SECRET) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        // Tạo chuỗi json web token từ account của user.
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(jwt)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
        return token;
    }
    public  Claims extractAllClaims(String token,  String JWT_SECRET) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy thông tin user từ jwt
    public String getUserAccountFromJWT(String token, String JWT_SECRET) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Date getExpired(String token, String JWT_SECRET) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }



    public boolean validateToken(String authToken, String JWT_SECRET) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            _logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            _logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            _logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            _logger.error("JWT claims string is empty.");
        } catch (Exception ignored) {

        }
        return false;
    }
    public String getAuthToken(String bearerToken, String TOKEN_START_WITH) {

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_START_WITH)) {
            return bearerToken.replace(TOKEN_START_WITH, "");
        }
        return bearerToken;
    }
    public String getJwtFromRequest(HttpServletRequest request, String TOKEN_START_WITH) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_START_WITH)) {
            return bearerToken.replace(TOKEN_START_WITH, "");
        }
        return null;
    }

    /*public static String getEmailFromRequest(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String jwt = jwtTokenProvider.getJwtFromRequest(httpServletRequest);
        *//*String account = jwtTokenProvider.getUserAccountFromJWT(jwt);
        String data ="";
        try {
            JSONObject jsonAccount = new JSONObject(account);
            data = jsonAccount.getString("data");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return data;*//*
        return jwtTokenProvider.getUserAccountFromJWT(jwt);
    }*/

}
