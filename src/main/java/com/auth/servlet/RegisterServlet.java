package com.auth.servlet;

import com.auth.dao.UserDAO;
import com.auth.dto.RegisterRequest;
import com.auth.dto.UserSessionDto;
import com.auth.exception.AuthException;
import com.auth.service.AuthService;
import com.auth.service.AuthServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RegisterServlet.class);

    private AuthService authService;

    @Override
    public void init() {
        this.authService = new AuthServiceImpl(new UserDAO());
    }

    /** Package-private setter used by unit tests to inject a mock. */
    void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }
        req.getRequestDispatcher("/register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        RegisterRequest registerReq = new RegisterRequest(
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("confirmPassword")
        );

        try {
            UserSessionDto sessionUser = authService.register(registerReq);

            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", sessionUser);
            session.setMaxInactiveInterval(30 * 60);

            resp.sendRedirect(req.getContextPath() + "/welcome.html?name="
                    + encode(sessionUser.getFirstName())
                    + "&email=" + encode(sessionUser.getEmail()));

        } catch (AuthException e) {
            log.warn("Registration failed [{}]: {}", e.getErrorCode(), e.getMessage());
            resp.sendRedirect(req.getContextPath()
                    + "/register.html?error=" + encode(e.getMessage()));
        }
    }

    private static boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("currentUser") != null;
    }

    private static String encode(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
