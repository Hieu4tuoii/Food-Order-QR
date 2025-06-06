package vn.hieu4tuoi.service;

import org.springframework.security.core.GrantedAuthority;
import vn.hieu4tuoi.common.TokenType;

import java.util.Collection;

public interface JwtService {
    String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);
    String generateRefreshToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);
    String extractUsername(String token, TokenType type);
}
