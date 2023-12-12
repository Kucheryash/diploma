package project.entity.enums;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    ROLE_BA, ROLE_SPEC;

    @Override
    public String getAuthority() {
        return name();
    }
}
