package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.dto.UserSessionDto;
import com.auth.exception.AuthException;

/**
 * Contract for authentication operations.
 *
 * Servlets depend on this interface — never on AuthServiceImpl directly.
 * This makes the implementation swappable and the servlets unit-testable
 * by injecting a mock AuthService without touching a database.
 */
public interface AuthService {

    /**
     * Validates the request, checks for duplicate email,
     * hashes the password, and persists the new user.
     *
     * @param req raw registration form data
     * @return session-safe DTO on success
     * @throws AuthException typed failure (VALIDATION_ERROR, EMAIL_ALREADY_EXISTS, DATABASE_ERROR)
     */
    UserSessionDto register(RegisterRequest req) throws AuthException;

    /**
     * Validates input, loads the user, and verifies the password.
     *
     * @param req raw login form data
     * @return session-safe DTO on success
     * @throws AuthException typed failure (VALIDATION_ERROR, INVALID_CREDENTIALS, DATABASE_ERROR)
     */
    UserSessionDto login(LoginRequest req) throws AuthException;
}
