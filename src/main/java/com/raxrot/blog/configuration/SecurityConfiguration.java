package com.raxrot.blog.configuration;

import com.raxrot.blog.exception.CustomAccessDeniedHandler;
import com.raxrot.blog.exception.CustomAuthenticationEntryPoint;
import com.raxrot.blog.security.JWTAuthenticationEntryPoint;
import com.raxrot.blog.security.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    public SecurityConfiguration(CustomAccessDeniedHandler accessDeniedHandler,
                                 CustomAuthenticationEntryPoint authenticationEntryPoint,
                                 UserDetailsService userDetailsService,
                                 JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                 JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
       return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/posts/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        http.exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
        http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public  PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails vlad= User.builder().username("vlad").password(passwordEncoder.encode("123")).roles("ADMIN").build();
        UserDetails daria= User.builder().username("daria").password(passwordEncoder.encode("123")).roles("USER").build();
        return new InMemoryUserDetailsManager(vlad,daria);
    }
     */
}
