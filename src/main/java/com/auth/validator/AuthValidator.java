package com.auth.validator;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.exception.AuthException;
import com.auth.exception.AuthException.ErrorCode;

import java.util.regex.Pattern;

public final class AuthValidator {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int NAME_MAX_LENGTH     = 50;

    private AuthValidator() {}

    // ── Registration validation ───────────────────────
    public static void validateRegister(RegisterRequest req) throws AuthException {

        requireNonBlank(req.getFirstName(),  "First name is required");
        requireNonBlank(req.getLastName(),   "Last name is required");
        requireNonBlank(req.getEmail(),      "Email address is required");
        requireNonBlank(req.getPassword(),   "Password is required");
        requireNonBlank(req.getConfirmPassword(), "Please confirm your password");

        if (req.getFirstName().trim().length() > NAME_MAX_LENGTH ||
            req.getLastName().trim().length()  > NAME_MAX_LENGTH) {
            fail("Name must not exceed " + NAME_MAX_LENGTH + " characters");
        }

        if (!EMAIL_PATTERN.matcher(req.getEmail().trim()).matches()) {
            fail("Please enter a valid email address");
        }

        if (req.getPassword().length() < PASSWORD_MIN_LENGTH) {
            fail("Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
        }

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            fail("Passwords do not match");
        }
    }

    // ── Login validation ─────────────────────────────
    public static void validateLogin(LoginRequest req) throws AuthException {

        requireNonBlank(req.getEmail(),    "Email address is required");
        requireNonBlank(req.getPassword(), "Password is required");

        if (!EMAIL_PATTERN.matcher(req.getEmail().trim()).matches()) {
            fail("Please enter a valid email address");
        }
    }

    // ── Helpers ───────────────────────────────────────
    private static void requireNonBlank(String value, String message) throws AuthException {
        if (value == null || value.trim().isEmpty()) {
            fail(message);
        }
    }

    private static void fail(String message) throws AuthException {
        throw new AuthException(ErrorCode.VALIDATION_ERROR, message);
    }
}
