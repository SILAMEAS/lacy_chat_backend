package com.lacy.chat.config;

import com.lacy.chat.modules.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class JwtUtil {
  final AppProperties appProperties;

  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getEmail())
        .claim("userId", user.getId())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + appProperties.getJwt_exp()))
        .signWith(Keys.hmacShaKeyFor(appProperties.getJwt_secret().getBytes()))
        .compact();
  }

  public String extractEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(appProperties.getJwt_secret().getBytes())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
