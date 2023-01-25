package com.example.superheroes.config;

import com.example.superheroes.filter.CustomAuthorizationFilter;
import com.example.superheroes.service.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final List<String> ignoreAuth = List.of(
            "/h2-console/**",
            "/v2/api-docs/**",
            "**swagger**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/resources/**",
            "/favicon.ico",
            "/js/**",
            "**/js/**",
            "/static/**"
    );

    @Autowired
    private JwtUtilService jwtUtilService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and().cors().and().csrf().disable()
                .authorizeHttpRequests(r -> r
                        .requestMatchers(ignoreAuth.stream().map(AntPathRequestMatcher::new).toArray(RequestMatcher[]::new)).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/superheroes**").hasAnyRole("USER", "STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/superheroes**").hasAnyAuthority("ROLE_USER_PRIVILEGE_WRITE", "ROLE_USER_PRIVILEGE_WRITE", "ROLE_ADMIN_PRIVILEGE_WRITE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/superheroes**").hasAnyAuthority("ROLE_USER_PRIVILEGE_WRITE", "ROLE_USER_PRIVILEGE_WRITE", "ROLE_ADMIN_PRIVILEGE_WRITE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/superheroes**").hasAuthority("ROLE_ADMIN_PRIVILEGE_WRITE")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .addFilterBefore(new CustomAuthorizationFilter(jwtUtilService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /*@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(ignoreAuth.stream().map(AntPathRequestMatcher::new).toArray(RequestMatcher[]::new));
    }*/

    private AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/api/v1/superheroes");
        return handler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
