
package com.interiorhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/projects",
                                "/api/projects/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.HEAD,
                                "/api/projects",
                                "/api/projects/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/api/**"
                        ).permitAll()

                        .requestMatchers("/error").permitAll()

                        .requestMatchers("/admin/login", "/csrf").permitAll()

                        .requestMatchers("/admin", "/admin/**").authenticated()

                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            PasswordEncoder passwordEncoder
    ) {
        String adminPassword = System.getenv("ADMIN_PASSWORD");

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable ADMIN_PASSWORD is not set"
            );
        }
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
