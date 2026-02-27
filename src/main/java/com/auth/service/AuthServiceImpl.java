package com.auth.service;

import com.auth.dao.UserDAO;
import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.dto.UserSessionDto;
import com.auth.exception.AuthException;
import com.auth.exception.AuthException.ErrorCode;
import com.auth.model.User;
import com.auth.validator.AuthValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private static final int BCRYPT_COST = 12;

    private final UserDAO userDAO;

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserSessionDto register(RegisterRequest req) throws AuthException {

        AuthValidator.validateRegister(req);

        String email = req.getEmail().trim().toLowerCase();

        if (userDAO.existsByEmail(email)) {
            log.warn("Registration rejected — email already exists: {}", email);
            throw new AuthException(ErrorCode.EMAIL_ALREADY_EXISTS,
                    "An account with this email already exists");
        }

        String hash = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(BCRYPT_COST));

        User user = new User(
                req.getFirstName().trim(),
                req.getLastName().trim(),
                email,
                hash
        );
        userDAO.insert(user);
        log.info("New user registered: id={} email={}", user.getId(), email);

        return toSessionDto(user);
    }

    @Override
    public UserSessionDto login(LoginRequest req) throws AuthException {

        AuthValidator.validateLogin(req);

        String email = req.getEmail().trim().toLowerCase();

        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed — email not found: {}", email);
                    return new AuthException(ErrorCode.INVALID_CREDENTIALS,
                            "Invalid email or password");
                });

        if (!BCrypt.checkpw(req.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed — wrong password for email: {}", email);
            throw new AuthException(ErrorCode.INVALID_CREDENTIALS,
                    "Invalid email or password");
        }

        log.info("User logged in: id={} email={}", user.getId(), email);
        return toSessionDto(user);
    }

    private static UserSessionDto toSessionDto(User user) {
        return new UserSessionDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
