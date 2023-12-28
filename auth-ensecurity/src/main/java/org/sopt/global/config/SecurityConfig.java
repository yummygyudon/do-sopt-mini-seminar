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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Redirect URL 들의 경우, 클라이언트분들과 협의하시면 됩니다.
     * (ex. Logout Success Url, redirectEndpoint 등의 Uri, ...)
     */

    private static final String[] WHITE_PATTERNS = {
//            "/", "/error", "/webjars/**",

            // Swagger
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",

            // Authentication
//            "/api/v1/auth/**",
//            "/api/v1/oauth/**",
            "/**",
            "/login/**",
            "/auth/**",
            "/oauth2/**",
            "/api/v1/oauth/**",
            "/api/v1/oauth2/**",
            "/api/v1/oauth/**",
            "/api/v1/auth/login/**",
            "/api/v1/auth/logout/**"
    };

    private final SocialAuthService socialAuthService;
    private final InternalAuthService internalAuthService;
    private final OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;
    private final OAuth2AuthSuccessHandler2 oAuth2AuthSuccessHandler2;
    private final OAuth2AuthFailureHandler oAuth2AuthFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;


//    @Bean
//    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
//        return new HttpCookieOAuth2AuthorizationRequestRepository();
//    }



//
//    private final static List<String> clients = Arrays.asList("google");
//
//    private final static String CLIENT_PROPERTY_KEY
//            = "spring.security.oauth2.client.registration.";
//
//    private final Environment env;
//
//    private ClientRegistration getRegistration(String client) {
//        String clientId = env.getProperty(
//                CLIENT_PROPERTY_KEY + client + ".client-id");
//
//        if (clientId == null) {
//            return null;
//        }
//
//        String clientSecret = env.getProperty(
//                CLIENT_PROPERTY_KEY + client + ".client-secret");
//
//        if (client.equals("google")) {
//            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
//                    .clientId(clientId).clientSecret(clientSecret).build();
//        }
//        return null;
//    }
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        List<ClientRegistration> registrations = clients.stream()
//                .map(c -> getRegistration(c))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        return new InMemoryClientRegistrationRepository(registrations);
//    }
//    @Bean
//    public OAuth2AuthorizedClientService authorizedClientService() {
//
//        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
//    }
//    @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(internalAuthService);
////        authenticationProvider.setPasswordEncoder(passwordEncoder);
//
//        ProviderManager providerManager = new ProviderManager(authenticationProvider);
//        providerManager.setEraseCredentialsAfterAuthentication(false);
//
//        return providerManager;
//    }
//
//    @Bean
//    JwtAuthenticationFilter customJwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(authenticationManager());
//    }

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
//                .headers(HeadersConfigurer::defaultsDisabled)
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
//                .authorizeHttpRequests((authorizeRequests) ->
//                        authorizeRequests
//                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                                .requestMatchers(WHITE_PATTERNS).permitAll()
//                                .requestMatchers("/api/v1/user/remove/**").hasAnyAuthority("MASTER")
//                                .requestMatchers("/api/v1/user/revoke/**").hasAnyAuthority("ADMIN", "MASTER")
//                                .requestMatchers("/api/v1/user/**").hasAuthority("MEMBER")
//                )
                .userDetailsService(internalAuthService)
                // OAuth2 Login 속성 관리 //
                .oauth2Login()
                .authorizationEndpoint()
//                /*
//                 * << 소셜 로그인 요청을 보내는 url >>
//                 * /oauth2/authorization/{provider}
//                 */
                .baseUri("/api/v1/oauth2/authorization/**")
//                .baseUri("/login/oauth2/code/**")
                .authorizationRequestRepository(new CookieAuthorizationRequestRepository())
                .and()
//                /*
//                 * << 소셜 인증 후 redirect 요청을 보내는 url >>
//                 * /login/oauth2/code/{provider}
//                 */
                .redirectionEndpoint()
////                .baseUri("/api/v1/oauth/callback/**")
//                .baseUri("/api/v1/oauth2/code/**")
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint().userService(socialAuthService)
                .and()
//                .successHandler(oAuth2AuthSuccessHandler)
                .successHandler(oAuth2AuthSuccessHandler2)
                .failureHandler(oAuth2AuthFailureHandler)
//                .oauth2Login((oauth2Login) ->
//                                oauth2Login
//                                        .successHandler(oAuth2AuthSuccessHandler)
//                                        .failureHandler(oAuth2AuthFailureHandler)
////                                        .authorizedClientRepository((OAuth2AuthorizedClientRepository) clientRegistrationRepository())
////                                        .authorizedClientService(authorizedClientService())
//                                        .authorizationEndpoint((authorizationEndPoint) ->
//                                                        authorizationEndPoint
//                                                                /*
//                                                                 * session이 아닌 jwt를 사용할 것이기 때문에
//                                                                 * 직접 구현한 cookieAuthorizationRequestRepository를 적용
//                                                                 * => cookie를 사용하는 방식으로 변경
//                                                                 */
////                                                .baseUri("/oauth2/authorize") // 소셜 로그인 url
//                                                                .baseUri("/api/v1/auth/login/social") // 소셜 로그인 url
//                                        )
//                                        .redirectionEndpoint((redirectEndpoint) ->
//                                                redirectEndpoint.baseUri("/oauth2/callback/*") // 소셜 인증 후 redirect url
//                                        )
//                                        // OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정
//                                        .userInfoEndpoint((userInfoEndPoint) ->
//                                                userInfoEndPoint
//                                                        .userService(socialAuthService)
//                                        )
//                )
                // Logout 관리 //
                ;

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
