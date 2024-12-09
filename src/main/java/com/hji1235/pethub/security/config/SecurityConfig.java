package com.hji1235.pethub.security.config;

import com.hji1235.pethub.security.filter.CustomLogoutFilter;
import com.hji1235.pethub.security.filter.JWTFilter;
import com.hji1235.pethub.security.service.RefreshService;
import com.hji1235.pethub.security.util.JWTUtil;
import com.hji1235.pethub.security.filter.LoginFilter;
import com.hji1235.pethub.security.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration configuration;
    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http.cors(cors -> cors
                .configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setMaxAge(3600L);
                    corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
                    return corsConfiguration;
                }));

        // CSRF 비활성화
        http.csrf(auth -> auth.disable());
        // Form 로그인 방식 비활성화
        http.formLogin(auth -> auth.disable());
        // http basic 인증 방식 비활성화
        http.httpBasic(auth -> auth.disable());

        // 경로별 인가 작업
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/join", "/reissue").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        // 로그인 필터 등록
        http.addFilterAt(new LoginFilter(authenticationManager(configuration), jwtUtil, refreshService), UsernamePasswordAuthenticationFilter.class);

        // 로그아웃 필터 등록
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshService), LogoutFilter.class);

        // 검증 필터 등록
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        
        // 세션 미사용 설정
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}
