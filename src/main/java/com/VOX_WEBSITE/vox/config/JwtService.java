package com.VOX_WEBSITE.vox.config;


import com.VOX_WEBSITE.vox.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "xwAMwqYqubUc1qR0A7x9Xe2RgXQT8+r+ebLh607NAoA=";

    public String extractName(String token) {
        return extractClaimу(token, Claims::get, "name").toString();
    }

    public String extractSecondName(String token) {
        return extractClaimу(token, Claims::get, "secondName").toString();
    }

    public Long extractUserID(String token) {
        return (Long) extractClaimу(token, Claims::get, "userID");
    }

    private <T> T extractClaimу(String token, BiFunction<Claims, String, T> clamsResolver, String claimName) {
        final Claims claims = extractAllClaimsFromToken(token);
        return clamsResolver.apply(claims, claimName);
    }


    //method to extract username(email) from the token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> clamsResolver){
        final Claims claims = extractAllClaimsFromToken(token);
        return clamsResolver.apply(claims);
    }


    public String generateToken(User userDetails) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", userDetails.getFirstName());
        extraClaims.put("secondName", userDetails.getSecondName());
        extraClaims.put("userID", userDetails.getId());
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Claims extractAllClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private @NotNull Key getKey() {
        byte[] byteKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(byteKey);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
