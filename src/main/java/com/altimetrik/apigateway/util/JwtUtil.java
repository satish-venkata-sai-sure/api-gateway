package com.altimetrik.apigateway.util;

import java.security.Key;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    
    public List<String> extractRoles(String token){
    	byte[] signingKey = Decoders.BASE64.decode(SECRET); 

        Jws<Claims> claims = Jwts.parserBuilder()
                                .setSigningKey(signingKey)
                                .build()
                                .parseClaimsJws(token);

        return claims.getBody().get("roles", List.class);
    }



    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
