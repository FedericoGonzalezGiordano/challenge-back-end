package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshExpiration;
    @Override
    public String generateToken(UserEntity userEntity) {
        return  buildToken(userEntity,jwtExpiration);
    }
    @Override
    public String generateRefreshToken(UserEntity userEntity) {
        return  buildToken(userEntity,refreshExpiration);
    }

    @Override
    public String extractUserName(String refreshToken) {
        try {
            final Claims jwtToken = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
            return jwtToken.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    @Override
    public boolean isTokenValid(final String refreshToken, UserEntity userEntity) {
        final String username=extractUserName(refreshToken);
        return (username.equals(userEntity.getEmail())) && !isTokenExpired(refreshToken);
    }

    private boolean isTokenExpired(final String refreshToken) {
        try {
            return extractExpiration(refreshToken).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }


    private Date extractExpiration(final String refreshToken) {
        try {
            final Claims jwtToken = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
            return jwtToken.getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        }
    }


    private String buildToken(final UserEntity userEntity, final long expiration) {
        long currentTime = System.currentTimeMillis();
        Date issuedAt = new Date(currentTime);
        Date expirationDate = new Date(currentTime + expiration);

        System.out.println("Building token:");
        System.out.println("Current time (ms): " + currentTime);
        System.out.println("IssuedAt: " + issuedAt);
        System.out.println("Will expire at: " + expirationDate);
        System.out.println("Expiration time (ms): " + expiration);

        String token = Jwts.builder()
                .id(userEntity.getId().toString())
                .claims(Map.of("name", userEntity.getName()))
                .subject(userEntity.getEmail())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();

        // Verificación del token generado
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        System.out.println("\nVerificación del token creado:");
        System.out.println("IssuedAt verificado: " + claims.getIssuedAt());
        System.out.println("Expiration verificado: " + claims.getExpiration());
        System.out.println("Tiempo hasta expiración (ms): " + (claims.getExpiration().getTime() - claims.getIssuedAt().getTime()));

        return token;
    }


    private SecretKey getSignInKey(){
        byte[]keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
