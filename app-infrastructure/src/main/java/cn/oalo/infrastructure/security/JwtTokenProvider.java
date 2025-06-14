package cn.oalo.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT令牌提供者
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成密钥
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 创建令牌
     *
     * @param userDetails 用户信息
     * @return 令牌
     */
    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // 获取当前用户ID
        Long userId = null;
        if (userDetails instanceof JwtUserDetails) {
            userId = ((JwtUserDetails) userDetails).getId();
        }
        
        // 将用户ID作为主题
        claims.put("sub", userId != null ? userId.toString() : userDetails.getUsername());
        
        // 将权限列表存入JWT
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("authorities", authorities);
        
        return generateToken(claims);
    }

    /**
     * 生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            log.error("JWT解析异常", e);
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            log.warn("Token中的主题不是有效的用户ID", e);
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 验证令牌是否有效
     *
     * @param token       令牌
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            
            // 如果是JwtUserDetails，则比较用户ID
            if (userDetails instanceof JwtUserDetails) {
                String userId = String.valueOf(((JwtUserDetails) userDetails).getId());
                return userId.equals(claims.getSubject()) && !isTokenExpired(claims);
            }
            
            // 否则比较用户名
            return userDetails.getUsername().equals(claims.getSubject()) && !isTokenExpired(claims);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    /**
     * 判断令牌是否过期
     *
     * @param claims 数据声明
     * @return 是否过期
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 刷新令牌
     *
     * @param token 令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        
        // 刷新令牌时不改变声明数据
        return generateToken(claims);
    }
} 