package com.futurevet.security;

import com.futurevet.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UsuarioRepository repo) {
        return email -> {
            var u = repo.email(email.strip())
                    .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
            return User.withUsername(u.email()).password(u.senha()).roles(u.role()).build();
        };
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        a -> a.requestMatchers("/login", "/registro", "/css/**", "/error",
                                        "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated())
                .formLogin(f -> f.loginPage("/login").usernameParameter("email").passwordParameter("senha")
                        .defaultSuccessUrl("/", true).permitAll())
                .logout(l -> l.logoutSuccessUrl("/login?logout").invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(e -> e.accessDeniedPage("/acesso-negado")).build();
    }
}