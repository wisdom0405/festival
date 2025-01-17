package com.beyond.festival.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    @Value("${jwt.expirationRt}")
    private int expirationRt;

    // token
    public String createToken(String email, String role){
        // Claims : 페이로드에 들어갈 사용자 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role",role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 생성시간
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L)) //만료시간 : 600분
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }
    // refresh Token
    public String createRefreshToken(String email, String role){
        // Claims : 페이로드에 들어갈 사용자 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role",role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 생성시간
                .setExpiration(new Date(now.getTime() + expirationRt * 60 * 1000L)) //만료시간 : 600분
                .signWith(SignatureAlgorithm.HS256, secretKeyRt)
                .compact();
        return token;
    }
}
