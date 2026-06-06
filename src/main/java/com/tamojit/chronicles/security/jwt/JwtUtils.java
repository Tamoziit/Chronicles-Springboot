package com.tamojit.chronicles.security.jwt;

import com.tamojit.chronicles.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    @Value("{auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("{auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication) {
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal(); // getting logged-in user

        List<String> roles = userPrincipal.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList(); // getting roles of the user

        // encoding JWT Token for user with an email with his/her details (id, roles), JWT Secret & start + expiration dates
        return Jwts.builder()
            .subject(userPrincipal.getEmail())
            .claim("id", userPrincipal.getId()) // setting data inside the token
            .claim("roles", roles).issuedAt(new Date()).expiration(new Date((new Date()).getTime() + expirationTime))
            .signWith(key())
            .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token);

            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
