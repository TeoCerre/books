package it.uniroma3.siw.SiwBooks.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public AccessDeniedHandler accessDeniedHandler() {
                AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
                handler.setErrorPage("/access-denied");
                return handler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**",
                                                                "/favicon.ico", "/register",
                                                                "/books/*/cover",
                                                                "/authors/*/photo", "/books/*", "/authors",
                                                                "/authors/**", "/login",
                                                                "/authorDetails.html",
                                                                "/bookImages/**", "/books/*/cover")
                                                .permitAll()
                                                .requestMatchers("/admin/**", "/admin/rest/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/default", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler(accessDeniedHandler())); 

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
