package com.he181464.be_class.jwt;

import com.he181464.be_class.entity.Account;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final AccountRepository accountRepository;

    private final TokenRepository tokenRepository;

    public String generateAccessToken(UserDetails userDetails) {

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .claim("role", role) // ROLE_LEARNER, ROLE_ADMIN
                .claim("accountId", account.getId())
                .setIssuer("engLessons-app")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 15 mins
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        boolean isExpire = Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody().getExpiration().before(new Date());

        return username.equals(userDetails.getUsername()) && !isExpire;
    }

}
