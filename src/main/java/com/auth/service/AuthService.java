package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.dto.UserSessionDto;
import com.auth.exception.AuthException;

public interface AuthService {

    
    UserSessionDto register(RegisterRequest req) throws AuthException;

    UserSessionDto login(LoginRequest req) throws AuthException;
}
