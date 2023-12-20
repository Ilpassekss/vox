package com.VOX_WEBSITE.vox.Services;
import com.VOX_WEBSITE.vox.Entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.security.Key;


@Service
@Slf4j
public class JwtService {

    //refresh token secret
    private final String SECRET_1;
    //access token secret
    private final String SECRET_2;

    public JwtService(@Value("${jwt.accessToken.secret}") String SECRET_1,
                      @Value("${jwt.refreshToken.secret}") String SECRET_2){
        this.SECRET_1 = SECRET_1;
        this.SECRET_2 = SECRET_2;
    }

    //for access token

    public String generateAccessToken(User userDetails) {

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("role", userDetails.getRole());
        extraClaims.put("userID", userDetails.getId());

        System.out.println(userDetails.getId());

        return generateAccessToken(extraClaims, userDetails);
    }

    private String generateAccessToken(Map<String, Object> extraClaims, User userDetails){

        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(new Date(System.currentTimeMillis() + 120000))//2 minutes access token live
                //.setExpiration(new Date(System.currentTimeMillis() + 300000))//5 minutes access token live
                .setExpiration(new Date(System.currentTimeMillis() + (24*60*60000)))//1 day access token live
                .signWith(getSignKey(SECRET_1), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUsernameFromAccessToken(String token) {
        return extractClaimFromAccessToken(token, Claims::getSubject);
    }

    public Date extractExpirationFromAccessToken(String token) {
        return extractClaimFromAccessToken(token, Claims::getExpiration);
    }

    public <T> T extractClaimFromAccessToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromAccessToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsFromAccessToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey(SECRET_1))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails){
        final String username = extractUsernameFromAccessToken(token);
        return (username.equals(userDetails.getUsername())) && !isAccessTokenExpired(token);
    }

    public boolean isAccessTokenExpired(String token){
        return extractExpirationFromAccessToken(token).before(new Date());
    }


    //for refresh token
    public String generateRefreshToken(User userDetails) {

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("userID", userDetails.getId());

        return generateRefreshToken(extraClaims, userDetails);
    }

    private String generateRefreshToken(Map<String, Object> extraClaims, User userDetails){

        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 300000))
                //.setExpiration(new Date(System.currentTimeMillis() + 2505600000L))//29 days token life
                .signWith(getSignKey(SECRET_2), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractClaimFromRefreshToken(token, Claims::getSubject);
    }

    public Date extractExpirationFromRefreshToken(String token) {
        return extractClaimFromRefreshToken(token, Claims::getExpiration);
    }

    public <T> T extractClaimFromRefreshToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsFromRefreshToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey(SECRET_2))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isRefreshTokenValid(String token, User userDetails){
        final String username = extractUsernameFromRefreshToken(token);
        return (username.equals(userDetails.getUsername())) && !isRefreshTokenExpired(token);
    }

    public boolean isRefreshTokenExpired(String token){
        return extractExpirationFromRefreshToken(token).before(new Date());
    }



    //for all tokens
    private Key getSignKey(String secret) {
        byte[] keyBytes= Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken,  Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_1)));
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken,  Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_2)));
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }



}
