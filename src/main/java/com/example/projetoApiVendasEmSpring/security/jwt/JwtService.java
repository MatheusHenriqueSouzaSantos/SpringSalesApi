package com.example.projetoApiVendasEmSpring.security.jwt;

import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.expiration}")
    private long expirationTimeMilliSeg;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private byte[] secretKeyBytes;

    private Key key;
    @PostConstruct
    public void postContruct(){
        secretKeyBytes= Base64.getDecoder().decode(secretKey);
        key= Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String generateJwt(UserDetailsImpl userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ expirationTimeMilliSeg))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateJwt(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
