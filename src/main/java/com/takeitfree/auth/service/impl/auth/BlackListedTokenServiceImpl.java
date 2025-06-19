package com.takeitfree.auth.service.impl.auth;

import com.takeitfree.auth.config.utils.JwtUtils;
import com.takeitfree.auth.models.BlackListedToken;
import com.takeitfree.auth.repositories.BlackListedTokenRepository;

import com.takeitfree.auth.service.BlackListedTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BlackListedTokenServiceImpl implements BlackListedTokenService {

    private final BlackListedTokenRepository blackListedTokenRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void addBlackListedToken(String token) {

        Date dateExpiration = this.jwtUtils.extractExpiration(token);

        BlackListedToken tokenExpired = new BlackListedToken();
        tokenExpired.setToken(token);
        tokenExpired.setExpirationDate(dateExpiration.toInstant());

        this.blackListedTokenRepository.save(tokenExpired);

    }

    @Override
    public boolean isBlackListedTokenFromLogOut(String token) {
        return this.blackListedTokenRepository.existsByToken(token);
    }
}
