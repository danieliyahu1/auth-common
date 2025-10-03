package com.akatsuki.auth.common.config;

import com.akatsuki.auth.common.security.JwtAuthenticationFilter;
import com.akatsuki.auth.common.security.SecurityProperties;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.authorization.SingleResultAuthorizationManager.permitAll;

@EnableConfigurationProperties(SecurityProperties.class)
@Configuration
public class CommonSecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final SecurityProperties properties;

    @Autowired
    public CommonSecurityConfig(JwtAuthenticationFilter jwtFilter, SecurityProperties properties) {
        this.jwtFilter = jwtFilter;
        this.properties = properties;
    }

    public HttpSecurity  commonSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/error").permitAll();

                    auth.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll();

                    properties.getPermitAllRoutes().forEach(route ->
                            auth.requestMatchers(route).permitAll());

                    auth.anyRequest().authenticated();

                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http;
    }
}
