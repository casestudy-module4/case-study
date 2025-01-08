package com.example.casestudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    //    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/home","/login","/register").permitAll()
//                        .requestMatchers("/admins/forgot-password", "/admins/reset-password").permitAll()
//                        .requestMatchers("/admins/login", "/logoutSuccessful", "/403", "/style/**", "/img/**").permitAll()
//                        .requestMatchers("/admins/**").hasAuthority("ROLE_ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .usernameParameter("username") // Tên trường username trong form
//                        .passwordParameter("password") // Tên trường password trong form
//                        .loginPage("/custom-login") // URL hiển thị trang login (do Controller xử lý)
//                        .loginProcessingUrl("/login") // URL xử lý logic đăng nhập (do Spring Security xử lý)
//                        .failureUrl("/custom-login?error=true") // URL khi đăng nhập thất bại
//                        .defaultSuccessUrl("/home", true)
//                        .requestMatcher(new AntPathRequestMatcher("/custom-login"))// URL khi đăng nhập thành công
//                        .permitAll()
//                )
//                .formLogin(form -> form
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .loginPage("/admins/login")
//                        .failureUrl("/admins/login?error=true")
//                        .loginProcessingUrl("/admins/login")
//                        .defaultSuccessUrl("/admins/statistics", true)
//                        .requestMatcher(new AntPathRequestMatcher("/admins/login"))
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout") // URL để logout
//                        .logoutSuccessUrl("/admins/login?logout=true") // URL sau khi logout thành công
//                        .addLogoutHandler((request, response, authentication) -> {
//                            // Thêm xử lý bổ sung nếu cần, ví dụ ghi log
//                            System.out.println("User logged out: " + (authentication != null ? authentication.getName() : "Anonymous"));
//                        })
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            // Xử lý sau khi logout thành công (nếu cần logic tùy chỉnh)
//                            response.sendRedirect("/admins/login?logout=true");
//                        })
//                        .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
//                        .invalidateHttpSession(true) // Vô hiệu hóa session hiện tại
//                        .permitAll()
//                )
//                .exceptionHandling(exception -> exception
//                        .accessDeniedPage("/403")
//                );
//
//        return http.build();
//    }
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home", "/login", "/register", "/style/**", "/img/**").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/custom-login")
                                .loginProcessingUrl("/login")
                                .failureUrl("/home?error=true")
                                .successHandler((request, response, authentication) -> {
                                    response.sendRedirect("/home?success=true");
                                })
//                                .defaultSuccessUrl("/home", true)
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admins/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admins/login", "/admins/forgot-password", "/admins/reset-password").permitAll()
                        .anyRequest().hasAuthority("ROLE_ADMIN")
                )
                .formLogin(form -> form
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/admins/login")
                        .loginProcessingUrl("/admins/login")
                        .failureUrl("/admins/login?error=true")
                        .defaultSuccessUrl("/admins/statistics", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admins/logout")
                        .logoutSuccessUrl("/admins/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
