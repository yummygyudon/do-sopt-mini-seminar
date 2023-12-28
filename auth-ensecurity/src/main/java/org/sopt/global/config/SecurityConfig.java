package org.sopt.global.config;

import lombok.RequiredArgsConstructor;
import org.sopt.application.auth.internal.InternalAuthService;
import org.sopt.application.auth.social.SocialAuthService;
import org.sopt.domain.CookieAuthorizationRequestRepository;
import org.sopt.global.filter.JwtAuthenticationFilter;
import org.sopt.global.filter.JwtExceptionFilter;
import org.sopt.global.handler.OAuth2AuthFailureHandler;
import org.sopt.global.handler.OAuth2AuthSuccessHandler;
import org.sopt.global.handler.OAuth2AuthSuccessHandler2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Redirect URL 들의 경우, 클라이언트분들과 협의하시면 됩니다.
     * (ex. Logout Success Url, redirectEndpoint 등의 Uri, ...)
     */
    private static final String[] WHITE_PATTERNS = {
            "/", "/error", "/webjars/**",

            // Swagger
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",

            // Authentication
            "/login/**",
            "/auth/**",
            "/oauth2/**",
            "/api/v1/auth/**", // 자체 로그인 요청
            "/api/v1/oauth2/**", // OAuth 소셜 로그인 요청
    };

    private final SocialAuthService socialAuthService;
    private final InternalAuthService internalAuthService;
    private final OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;
    private final OAuth2AuthSuccessHandler2 oAuth2AuthSuccessHandler2;
    private final OAuth2AuthFailureHandler oAuth2AuthFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Security 설정 : RestController 적용을 위한 설정 //
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Request 관리 //
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(WHITE_PATTERNS).permitAll()
                .antMatchers("/api/v1/user/remove/**").access("hasAnyAuthority('MASTER')")
                .antMatchers("/api/v1/user/revoke/**").access("hasAnyAuthority('ADMIN','MASTER')")
                .antMatchers("/api/v1/user/**").access("hasAuthority('MEMBER')")
                .anyRequest().authenticated()
                .and()
                .userDetailsService(internalAuthService)

                // OAuth2 Login 속성 관리 //
                .oauth2Login()
                .authorizationEndpoint()
                /*
                 * << 소셜 로그인 요청을 보내는 url >>
                 */
                .baseUri("/api/v1/oauth2/authorization/**")
                .authorizationRequestRepository(new CookieAuthorizationRequestRepository())
                .and()
                /*
                 * << 소셜 인증 후 redirect 요청을 보내는 url >>
                 */
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint().userService(socialAuthService)
                .and()
//                .successHandler(oAuth2AuthSuccessHandler)
                .successHandler(oAuth2AuthSuccessHandler2)
                .failureHandler(oAuth2AuthFailureHandler)
                ;

        // Logout 관리 //
        httpSecurity
                .logout((logoutRequest) ->
                        logoutRequest
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )
        ;
        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }
}
