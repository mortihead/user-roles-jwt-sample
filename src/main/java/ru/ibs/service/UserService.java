package ru.ibs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.dto.User;
import ru.ibs.enums.RoleEnum;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public User getUser(@AuthenticationPrincipal Jwt jwt) {
        // Извлекаем claims из JWT
        String login = jwt.getClaimAsString("preferred_username"); // Логин
        String email = jwt.getClaimAsString("email"); // Email
        String firstName = jwt.getClaimAsString("given_name"); // Имя
        String lastName = jwt.getClaimAsString("family_name"); // Фамилия
        String middleName = jwt.getClaimAsString("middle_name"); // Отчество

        // Извлекаем роли из JWT-токена
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Set<String> roles = Set.copyOf((Collection<String>) realmAccess.get("roles"));

        // Создаем UserDto с помощью Builder
        return User.builder()
                .login(login)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .roles(roles)
                .build();
    }

    public User getUser() {
        // Получаем объект Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что authentication является JwtAuthenticationToken
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            // Получаем объект Jwt из authentication
            Jwt jwt = jwtAuthentication.getToken();
            return getUser(jwt);
        } else {
            // Если authentication не является JwtAuthenticationToken, возвращаем null или выбрасываем исключение
            throw new IllegalArgumentException("Unsupported authentication type: " + authentication.getClass());
        }
    }


    /**
     * Проверяет, является ли пользователь менеджером Toyota.
     *
     * @param user пользователь.
     * @return true, если пользователь является менеджером Toyota, иначе false.
     */
    public boolean isToyotaManager(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> RoleEnum.MANAGER_TOYOTA.getRoleName().equalsIgnoreCase(role));
    }

    /**
     * Проверяет, является ли пользователь менеджером Nissan.
     *
     * @param user пользователь.
     * @return true, если пользователь является менеджером Nissan, иначе false.
     */
    public boolean isNissanManager(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> RoleEnum.MANAGER_NISSAN.getRoleName().equalsIgnoreCase(role));
    }

    /**
     * Проверяет, является ли пользователь админом.
     *
     * @param user пользователь.
     * @return true, если пользователь является админом, иначе false.
     */
    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> RoleEnum.ADMIN.getRoleName().equalsIgnoreCase(role));
    }
}
