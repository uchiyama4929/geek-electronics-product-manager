package com.example.demo.test;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Position;
import com.example.demo.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManagerTestFactory {

    public static Manager createManager(Long managerId, Long storeId, Long positionId, Long permissionId, String roleName) {
        Manager manager = mock(Manager.class);
        Store store = mock(Store.class);
        Position position = mock(Position.class);
        Permission permission = mock(Permission.class);

        when(manager.getId()).thenReturn(managerId);
        when(manager.getStore()).thenReturn(store);
        when(store.getId()).thenReturn(storeId);
        when(manager.getPosition()).thenReturn(position);
        when(position.getId()).thenReturn(positionId);
        when(manager.getPermission()).thenReturn(permission);
        when(permission.getId()).thenReturn(permissionId);
        when(permission.getName()).thenReturn(roleName);

        when(manager.getFirstName()).thenReturn("立樹");
        when(manager.getLastName()).thenReturn("内山");
        when(manager.getEmail()).thenReturn("manager@example.com");
        when(manager.getPhoneNumber()).thenReturn("123-456-7890");
        when(manager.getPassword()).thenReturn("password");

        return manager;
    }

    public static Page<Manager> createManagerPage() {
        return new PageImpl<>(Collections.emptyList());
    }

    public static Authentication createAuthenticationWithUserDetails(Manager manager) {
        UserDetails userDetails = new User(
                manager.getEmail(),
                manager.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(manager.getPermission().getName()))
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        return authentication;
    }
}
