package com.example.playgroundmanage.auth;



import com.example.playgroundmanage.exception.IsExpiredToken;
import com.example.playgroundmanage.repository.TokenRepository;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.service.TokenService;
import com.example.playgroundmanage.service.UserService;
import com.example.playgroundmanage.vo.RefreshToken;
import com.example.playgroundmanage.vo.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Authentication authentication) {
        Claims claims = getTokenClaims(authentication);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getTokenExpiresIn(3600))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        Claims claims = getTokenClaims(authentication);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getTokenExpiresIn(864000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenInfo generateToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        return TokenInfo.builder()
                .grantType("USER")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RefreshToken getRefreshToken(String username) {
        return tokenService.getRefreshToken(username);
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        User user = userService.getUser(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Date getTokenExpiresIn(long nowToAfterSecond) {
        long now = new Date().getTime();
        return new Date(now + nowToAfterSecond);
    }

    private Claims getTokenClaims(Authentication authentication) {
        String authorization = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("auth", authorization);
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            claims.put("provider", "own");
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            claims.put("provider", "oauth");
        }
        return claims;
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
