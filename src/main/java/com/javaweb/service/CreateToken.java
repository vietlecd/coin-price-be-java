package com.javaweb.service;

import com.javaweb.model.LoginRequest;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class CreateToken {
    private final static String SECRET = System.getenv("JWT_SECRET");

    public static String createToken(String username, String password) throws JOSEException {
        System.out.println(SECRET);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("password", password)
                .issuer("MK")
                .expirationTime(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .build();

        JWSSigner signer = new MACSigner(SECRET.getBytes(StandardCharsets.UTF_8));

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public static LoginRequest decodeToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new MACVerifier(SECRET.getBytes(StandardCharsets.UTF_8));

        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expirationTime = claims.getExpirationTime();

            System.out.println("expirationTime: " + expirationTime);

            if (expirationTime != null && new Date().after(expirationTime)) {
                return new LoginRequest(claims.getSubject(), "expired");
            }

            return new LoginRequest(claims.getSubject(), claims.getStringClaim("password"));
        } else {
            throw new JOSEException("Invalid JWT");
        }
    }

    public static boolean validateToken(String token) {
        return true;
    }
}
