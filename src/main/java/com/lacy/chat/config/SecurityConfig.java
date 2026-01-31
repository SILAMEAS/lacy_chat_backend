package com.lacy.chat.config;

import com.lacy.chat.modules.user.service.CustomOAuth2UserService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler successHandler;
  private final JwtAuthenticationFilter jwtFilter;
  private final AppProperties appProperties;

  public SecurityConfig(
          CustomOAuth2UserService service,
          OAuth2SuccessHandler handler,
          JwtAuthenticationFilter jwtFilter,
          AppProperties appProperties) {
    this.oAuth2UserService = service;
    this.successHandler = handler;
    this.jwtFilter = jwtFilter;
    this.appProperties = appProperties;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(appProperties.getCors_origin()));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    // Public paths
                    .requestMatchers("/", "/oauth2/**", "/login/**", "/h2-console/**").permitAll()
                    // API endpoints require authentication
                    .requestMatchers("/api/**").authenticated()
                    // Any other paths are public
                    .anyRequest().permitAll()
            )
            .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                    .successHandler(successHandler)
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response,
                                               org.springframework.security.core.AuthenticationException authException) -> {

                      String uri = request.getRequestURI();

                      // API call: return 401
                      if (uri.startsWith("/api/")) {
                        response.sendError(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                      } else {
                        // Web page: redirect to OAuth login
                        response.sendRedirect("/oauth2/authorization/google");
                      }
                    })
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // Disable frame options for H2 console
    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

    return http.build();
  }
}
