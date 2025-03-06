package ru.ibs.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class User {

    private String login;       // Логин пользователя
    private String email;      // Email пользователя
    private String lastName;   // Фамилия
    private String firstName;  // Имя
    private String middleName; // Отчество
    private Set<String> roles; // Роли пользователя (множество для уникальности)
}