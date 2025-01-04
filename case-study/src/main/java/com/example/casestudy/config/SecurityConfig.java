package com.example.casestudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admins/forgot-password", "/admins/reset-password").permitAll()
                        .requestMatchers("/admins/login", "/logoutSuccessful", "/403", "/style/**", "/img/**").permitAll()
                        .requestMatchers("/admins/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/admins/login")
                        .failureUrl("/admins/login?error=true")
                        .loginProcessingUrl("/admins/login")
                        .defaultSuccessUrl("/admins/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để logout
                        .logoutSuccessUrl("/admins/login?logout=true") // URL sau khi logout thành công
                        .addLogoutHandler((request, response, authentication) -> {
                            // Thêm xử lý bổ sung nếu cần, ví dụ ghi log
                            System.out.println("User logged out: " + (authentication != null ? authentication.getName() : "Anonymous"));
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // Xử lý sau khi logout thành công (nếu cần logic tùy chỉnh)
                            response.sendRedirect("/admins/login?logout=true");
                        })
                        .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
                        .invalidateHttpSession(true) // Vô hiệu hóa session hiện tại
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                );

        return http.build();
    }

}
