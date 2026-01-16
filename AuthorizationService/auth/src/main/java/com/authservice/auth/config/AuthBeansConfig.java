package com.authservice.auth.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import com.authservice.auth.jwt.JwtUtil;

import reactor.netty.http.client.HttpClient;

@Configuration
public class AuthBeansConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean 
    AuthenticationManager authenticationManager(HttpSecurity http,
         PasswordEncoder encoder,
          UserDetailsService uds) throws Exception{
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(uds).passwordEncoder(encoder);
        return authBuilder.build();
    }
    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(3));

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
    @Bean("publicWebClient")
    public WebClient publicWebClient(WebClient.Builder builder) {
        return builder.build();
    }
    @Bean(name = "securedWebClient")
    public WebClient securedWebClient(WebClient.Builder builder, JwtUtil jwtUtil) {
        return builder
            .filter((request, next) -> {
                String token = jwtUtil.getServiceToken();
                if (token != null) {
                    ClientRequest newRequest = ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build();
                    return next.exchange(newRequest);
                }
                return next.exchange(request);
            })
            .build();
    }
}
