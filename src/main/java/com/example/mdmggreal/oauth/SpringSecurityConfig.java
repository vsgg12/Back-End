package com.example.mdmggreal.oauth;

import com.example.mdmggreal.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

    private final MemberService memberService;
    private static final String[] AUTH_WHITELIST = {
            "/oauth2/**", "/api/users/signup/**", "/api/users/callback/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests -> authorizeRequests
//                        .requestMatchers("/api/**").hasRole(Role.USER.name())
                        .requestMatchers(AUTH_WHITELIST).permitAll())
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService((memberService))));


        return http.build();
    }

}