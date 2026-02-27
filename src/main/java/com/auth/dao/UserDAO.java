package com.auth.dao;

import com.auth.config.DatabaseConfig;
import com.auth.exception.AuthException;
import com.auth.exception.AuthException.ErrorCode;
import com.auth.model.User;

import java.sql.*;
import java.util.Optional;


public class UserDAO {

    
    protected Connection getConnection() throws SQLException {
        return DatabaseConfig.getDataSource().getConnection();
    }

   
    public void insert(User user) throws AuthException {
        final String sql =
            "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) user.setId(keys.getInt(1));
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AuthException(ErrorCode.EMAIL_ALREADY_EXISTS,
                    "An account with this email already exists", e);
        } catch (SQLException e) {
            throw new AuthException(ErrorCode.DATABASE_ERROR,
                    "Failed to create user account", e);
        }
    }

    public Optional<User> findByEmail(String email) throws AuthException {
        final String sql = "SELECT * FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new AuthException(ErrorCode.DATABASE_ERROR, "Failed to look up user", e);
        }
    }

    public boolean existsByEmail(String email) throws AuthException {
        final String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new AuthException(ErrorCode.DATABASE_ERROR,
                    "Failed to check email existence", e);
        }
    }

    
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}
