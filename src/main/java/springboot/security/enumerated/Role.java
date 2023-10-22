package springboot.security.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static springboot.security.enumerated.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    // Adding permissions to roles
    USER(Collections.EMPTY_SET),
    MANAGER(Set.of(
            MANAGER_READ,
            MANAGER_UPDATE,
            MANAGER_DELETE,
            MANAGER_CREATE
    )),
    ADMIN(Set.of(
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            ADMIN_CREATE,
            MANAGER_READ,
            MANAGER_UPDATE,
            MANAGER_DELETE,
            MANAGER_CREATE
    ));

    // For lombok Getter and Constructor
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        // Get role permissions
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(
                        permission.getPermission()
                ))
                .collect(Collectors.toList());

        // We always have to add prefix when working with Spring authorities
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
