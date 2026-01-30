package com.example.projetoApiVendasEmSpring.security.jwt;

import com.example.projetoApiVendasEmSpring.security.SecurityUtils;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.security.UserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final SecurityUtils securityUtils;

    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, SecurityUtils securityUtils, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.securityUtils = securityUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization =request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String token=authorization.substring(7);

        Claims claims= jwtService.validateJwt(token);

        if(!securityUtils.verifyUserIsActiveByEmail(claims.getSubject())){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\" : \"The User is inactive\"");
            return;
        }
        Authentication authentication;
        try{
            UserDetailsImpl userDetails=(UserDetailsImpl) userDetailsService.loadUserByUsername(claims.getSubject());
            List<String> roles=(List<String>) claims.get("roles", List.class);
            List<SimpleGrantedAuthority> authorities=roles.stream().map((i)->new SimpleGrantedAuthority(i.toString()))
                    .toList();
            authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    authorities
            );
        }
        catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\" : \"The token in Incorrectly formatted\"");
            return;
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }
}
