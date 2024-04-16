package com.example.VolunteerWebApp.security;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;


@Service
public class JwtProvider {
    private KeyStore keyStore;

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceStream = getClass().getResourceAsStream("/spring-https.jks");
            keyStore.load(resourceStream, "password".toCharArray());
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error, exception occurred while loading keystore.");
        }
    }
    public String generateAuthToken(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenByUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public PrivateKey getPrivateKey(){
        try {
            return (PrivateKey) keyStore.getKey("spring-https", "password".toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error, exception occurred while retrieving private key from keystore.");
        }
    }

    public boolean validateToken(String jwt){
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    public PublicKey getPublicKey(){
        try {
            return keyStore.getCertificate("spring-https").getPublicKey();
        } catch (KeyStoreException e) {
            throw new RuntimeException("Error, exception occurred while retrieving public key from keystore.");
        }
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
