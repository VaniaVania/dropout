package com.ivan.restapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles()
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/authorize").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Не создавать сессии на стороне сервера
                .maximumSessions(1) // Максимальное количество активных сессий для одного пользователя
                .maxSessionsPreventsLogin(true) // Запретить новую аутентификацию при достижении максимального количества сессий
                .expiredUrl("/authorized") // URL для перенаправления при истечении срока действия сессии
                .and()
                .and()
                .logout()
                .logoutUrl("/myLogout") // URL для выполнения выхода
                .logoutSuccessUrl("/") // URL для перенаправления после выхода
                .invalidateHttpSession(true) // Инвалидировать сессию после выхода
                .deleteCookies("JSESSIONID"); // Удалить куки с идентификатором сессии


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
