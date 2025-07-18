package org.jane.gtelinternship.common.security;

import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.domain.model.UserRole;
import org.jane.gtelinternship.users.repo.UserStorageFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private static final String ROLE_PREFIX = "ROLE_";
    private final UserStorageFacade usageStorageFacade;

    public MyUserDetailsService(UserStorageFacade usageStorageFacade) {this.usageStorageFacade = usageStorageFacade;}

    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role(UserRole.ADMIN.name()).implies(UserRole.USER.name())
                .role(UserRole.ADMIN.name()).implies(UserRole.USER.name())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserModel userModel = usageStorageFacade.fetchUserByEmail(username);

        if (userModel != null) {
            return new User(
                userModel.id().toString(),
                userModel.password(),
                getAuthorities(userModel)
            );

        } else {
            System.out.println("User with id " + username + " not found");
            return null;
        }
    }

    Collection<GrantedAuthority> getAuthorities(UserModel user) {
        if (!user.approved()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + user.role().name()));
        }
    }
}
