package com.futurevet.security;

import com.futurevet.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(HttpSecurity http, JwtAuthFilter jwtAuthFilter,
            @Qualifier("corsConfigurationSource") CorsConfigurationSource cors) throws Exception {
        http.securityMatcher("/api/**")
                .cors(c -> c.configurationSource(cors))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> res.sendError(401, "Não autenticado"))
                        .accessDeniedHandler((req, res, ex) -> res.sendError(403, "Acesso negado")));
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}