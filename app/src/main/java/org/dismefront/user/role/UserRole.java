package org.dismefront.user.role;

public enum UserRole {
    STANDARD("STANDARD_USER"),
    VIP("VIP_USER"),
    PREMIUM("PREMIUM_USER"),
    MANAGER("SERVICE_MANAGER"),
    SUPER_ADMIN("SUPER_ADMIN");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
