package com.javaweb.service;

import com.javaweb.model.LoginRequest;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class CreateToken {
    private static final String SECRET = "R7h9YcUjMKL3zNHdGQwaXtFZ12pJObAy8fQm4slkWbvPIRzDqXC3v8MrZNfjTSY5HtuwxpabVdLoUeKJFhTAm7zg3jYW8XrNMPkV6QLcBxZoHREWUSCv5aqnwDioJP7XpLZB3tr4hdz9eOsvymcgKiAXNfRJshE2GHTVBYrZ1dQFPbwLVru5odX9WvU1CTQz";

    public static String createToken(String username, String password) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("password", password)
                    .issuer("MK")
                    .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                    .build();

            JWSSigner signer = new MACSigner(SECRET.getBytes(StandardCharsets.UTF_8));

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "false, there's some error";
        }
    }

    public static LoginRequest decodeToken(String token) {
        try {
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
        } catch (Exception e) {
            System.out.println("Error decoding token: " + e.getMessage());
            return new LoginRequest("error", "error");
        }
    }

    public static boolean validateToken(String token) {
        return true;
    }
}
