package com.eventoslive.eventosliveapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
        boolean isOrganizer = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ORGANIZER"));

        if (isAdmin) {
            response.sendRedirect("/admin/home");
        } else if (isOrganizer) {
            response.sendRedirect("/organizer/home");
        } else {
            response.sendRedirect("/user/home");
        }
    }
}
