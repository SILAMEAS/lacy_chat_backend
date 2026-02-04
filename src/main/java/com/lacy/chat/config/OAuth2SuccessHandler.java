package com.lacy.chat.config;

import com.lacy.chat.modules.user.model.User;
import com.lacy.chat.modules.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {
    private final AppProperties appProperties;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(UserRepository repo, JwtUtil jwtUtil,AppProperties appProperties) {
        this.userRepository = repo;
        this.jwtUtil = jwtUtil;
        this.appProperties=appProperties;
    }

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        assert oauthUser != null;
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
        String token = jwtUtil.generateToken(user);

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(Math.toIntExact(appProperties.getJwt_exp() / 1000));
        response.addCookie(cookie);

        response.sendRedirect(
                appProperties.getUrl_redirect() + token
        );
    }
}

