package ru.ibs.enums;


public enum RoleEnum {
    ADMIN("admin"),
    USER("user"),
    MODERATOR("moderator"),
    MANAGER_TOYOTA("manager_toyota"),
    MANAGER_NISSAN("manager_nissan");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    /**
     * Получить Role по имени роли.
     *
     * @param roleName Имя роли (например, "admin").
     * @return Соответствующий Role или null, если роль не найдена.
     */
    public static RoleEnum fromRoleName(String roleName) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.roleName.equals(roleName)) {
                return roleEnum;
            }
        }
        return null;
    }
}
