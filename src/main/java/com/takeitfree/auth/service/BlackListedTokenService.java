package com.takeitfree.auth.service;

public interface BlackListedTokenService {
    void addBlackListedToken(String token);
    boolean isBlackListedTokenFromLogOut(String token);
}
