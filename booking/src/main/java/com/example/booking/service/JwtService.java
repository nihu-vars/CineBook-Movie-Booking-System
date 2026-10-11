package com.example.booking.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    private String secretKey = ""; //This variable stores the secret key used to sign and verify your JWTs.

    public JwtService() throws NoSuchAlgorithmException {
        //this constructor is being used to generate secret key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGenerator.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    }
    public String generateToken(String userEmail) {
        Map<String, Object> claims = new HashMap<>();

        return  Jwts.builder()
                .claims().add(claims)
                .subject(userEmail)    //userEmail here is username
                .issuedAt((new Date(System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis() + 60*60*30))   //108000 milliseconds
                .and().signWith(getKey()).compact();
    }

    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //to get all the information from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload(); //this will return all the information of the token
    }

    //to extract a particular claim like subject only, expiration date only etc
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // to extract the subject from the token
    //extracting username from the token
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // to extract the expirationDate from the token
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // to check whether token is expired
    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());  //before() asks: "Is the expiration date BEFORE the current time?"

    }

    //to validate the token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractSubject(token);
        return  username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }




}
