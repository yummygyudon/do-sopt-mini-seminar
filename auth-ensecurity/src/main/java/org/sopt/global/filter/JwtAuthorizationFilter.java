package org.sopt.global.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.interfaces.MiminarUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter { //extends BasicAuthenticationFilter
//
//    private final MiminarUserRepository userRepository;
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MiminarUserRepository userRepository) {
//        super(authenticationManager);
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        // JWT Token in header -> Token Validate
//
//        doFilterInternal(request, response, chain);
//    }
}
