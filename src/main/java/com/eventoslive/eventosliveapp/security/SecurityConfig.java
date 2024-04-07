package com.eventoslive.eventosliveapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eventoslive.eventosliveapp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider()) // Añade esta línea
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/admin/**").hasAuthority("ADMIN")
                        .antMatchers("/organizer/**").hasAuthority("ORGANIZER")
                        .antMatchers("/user/**").hasAuthority("USER")
                        .antMatchers("/forum", "/login", "/users/register","/users/registerConfirm","/users/check-availability", "/css/**", "/js/**", "/images/**", "/h2-console/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/login?error=true") // Manejo de credenciales login
                        .successHandler(authenticationSuccessHandler()) // Usar el AuthenticationSuccessHandler
                        // personalizado
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .exceptionHandling(exception -> exception
                                .accessDeniedPage("/login") // esta línea maneja excepciones de acceso
                )
                .csrf(csrf -> csrf.ignoringAntMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

}