package ru.ibs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Настройка фильтров безопасности в приложении
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/version").permitAll()
                        .requestMatchers("/api/v1/cars").hasRole("users-jwt-app")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .csrf().disable(); // Отключите CSRF для упрощения примера
        return http.build();
    }

    /**
     * Как работает JwtAuthenticationConverter?
     * В Spring Security JwtAuthenticationConverter настраивается с помощью метода jwtAuthenticationConverter().
     * Извлечение ролей:
     * - JWT-токен содержит информацию о ролях в поле realm_access.roles (для ролей Realm)
     * или resource_access.{client}.roles (для ролей клиента).
     * - JwtAuthenticationConverter извлекает эти роли и преобразует их в объекты GrantedAuthority.
     * - Создание объекта Authentication:
     * На основе JWT и извлечённых ролей создаётся объект JwtAuthenticationToken,
     * который реализует интерфейс Authentication.
     * <p>
     * Использование в Spring Security:
     * <p>
     * Объект Authentication передаётся в Spring Security, где используется для проверки прав доступа.
     *
     * @return
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Извлечение ролей из JWT-токена
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) {
                return List.of();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });
        return converter;
    }

}
