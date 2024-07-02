package com.example.mdmggreal.global.security;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getHeader("Authorization") != null) {

            String authentication = request.getHeader("Authorization");

            if (authentication != null || authentication.startsWith("Bearer ")) {
                try {
                    if (JwtUtil.validateToken(authentication)) {
                        Long memberId  = JwtUtil.getMemberId(authentication);

                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberId.toString());

                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                } catch (CustomException e) {
                    setErrorResponse(response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, CustomException e) throws IOException {
        response.setStatus(e.getErrorCode().getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseEntity errorResponse = ErrorResponseEntity.builder()
                .status(e.getErrorCode().getHttpStatus().value())
                .code(e.getErrorCode().name())
                .message(e.getErrorCode().getMessage())
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}