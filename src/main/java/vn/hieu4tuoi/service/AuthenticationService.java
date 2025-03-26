package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.SignInRequest;
import vn.hieu4tuoi.dto.respone.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);
    TokenResponse getRefreshToken(String request);
}
