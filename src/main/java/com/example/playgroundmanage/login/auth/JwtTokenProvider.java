package com.example.playgroundmanage.login.auth;



import com.example.playgroundmanage.login.service.TokenService;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.login.vo.RefreshToken;
import com.example.playgroundmanage.login.vo.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final Long ACCESS_TOKEN_EXPIRATION = 60 * 60L;
    public static final Long REFRESH_TOKEN_EXPIRATION = 30 * 24 * 60 * 60L;

    private final Key key;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication, Long expirationFromCreated) {
        Claims claims = generateTokenClaims(authentication);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getTokenExpiresIn(expirationFromCreated))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims generateTokenClaims(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        String authorization = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claims.put("auth", authorization);

        String provider = determineProvider(authentication);
        claims.put("provider", provider);

        return claims;
    }

    private String determineProvider(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return "own";
        }
        if (authentication instanceof OAuth2AuthenticationToken) {
            return "oauth";
        }
        log.info("unknown token");
        throw new IllegalArgumentException();
    }


    public TokenInfo generateAccessAndRefreshTokens(Authentication authentication) {
        String accessToken = generateToken(authentication, ACCESS_TOKEN_EXPIRATION);
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRATION);

        return TokenInfo.builder()
                .grantType("USER")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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

        // Check the provider information and create the appropriate Authentication object
        if ("own".equals(claims.get("provider"))) {
            // Own provider (UsernamePasswordAuthenticationToken)
            return new UsernamePasswordAuthenticationToken(user, "", authorities);
        } else if ("oauth".equals(claims.get("provider"))) {
            // OAuth provider (OAuth2AuthenticationToken)
            OAuth2User oauth2User = new DefaultOAuth2User(
                    authorities,
                    null,  // Replace with your OAuth user attributes
                    "username"            // Replace with the username attribute key
            );
            return new OAuth2AuthenticationToken(oauth2User, authorities, "oauth-client");
        } else {
            throw new IllegalArgumentException("Unknown provider type in the token");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
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


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
