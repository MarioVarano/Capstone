package it.epicode.backend.capstone.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

//CLASSE CHE GENERA E CONTROLLA IL TOKEN (JWT)
@Component
public class JwtUtils {

    @Value("${jwt.key}")
    private String securityKey;
    @Value("${jwt.expirationMs}")
    private long expirationMs;

    public String generateToken(Authentication auth) {
        byte[] keyBytes = securityKey.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        var user = (SecurityUserDetails) auth.getPrincipal();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setIssuer("MySpringApplication")
                .setExpiration(new Date(new Date().getTime() + expirationMs))
                .signWith(key)
                //ESISTONO I CLAIM OVVERO SONO INFORMAZIONI AGGIUNTIVE CHE POSOSNO ESSERE AGGIUNTE AL TOKEN .claim("professore dell'aula", "Mauro")
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            byte[] keyBytes = securityKey.getBytes();
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            // Preleviamo i claims dal token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();

            // Verifichiamo se la data di scadenza è prima della data attuale
            if (expirationDate.before(new Date())) {
                throw new JwtException("Token expired");
            }

            // Verifichiamo l'issuer
            String issuer = claims.getIssuer();
            if (!"MySpringApplication".equals(issuer)) {
                throw new JwtException("Invalid issuer");
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        byte[] keyBytes = securityKey.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
