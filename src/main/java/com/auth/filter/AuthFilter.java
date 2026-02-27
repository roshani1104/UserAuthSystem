package com.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * AuthFilter — guards every protected route.
 *
 * Sits in front of all requests. If the user has no valid session,
 * they get redirected to login instead of reaching the page.
 *
 * Public paths (login, register, css, etc.) are explicitly skipped.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws java.io.IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI()
                             .substring(request.getContextPath().length());

        // ── Public paths — always allow through ──────────────────────────────
        if (isPublic(path)) {
            chain.doFilter(req, res);
            return;
        }

        // ── Check session ─────────────────────────────────────────────────────
        HttpSession session  = request.getSession(false);
        boolean     loggedIn = session != null
                            && session.getAttribute("currentUser") != null;

        if (loggedIn) {
            chain.doFilter(req, res);   // ✅ logged in — let them through
        } else {
            // ❌ not logged in — redirect to login
            response.sendRedirect(request.getContextPath()
                    + "/login.html?error=Please+log+in+to+continue");
        }
    }

    private boolean isPublic(String path) {
        return path.equals("/login.html")
            || path.equals("/register.html")
            || path.equals("/welcome.html")
            || path.equals("/login")
            || path.equals("/register")
            || path.equals("/index.html")
            || path.equals("/")
            || path.startsWith("/css/")
            || path.startsWith("/js/");
    }
}
