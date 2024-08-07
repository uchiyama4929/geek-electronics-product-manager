package com.example.demo.service;

import com.example.demo.entity.Manager;
import com.example.demo.repository.ManagerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    public CustomUserDetailsService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByEmail(email);
        if (manager == null) {
            throw new UsernameNotFoundException("Manager not found");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(manager.getRole()));

        return new org.springframework.security.core.userdetails.User(
                manager.getEmail(),
                manager.getPassword(),
                true, true, true, true,
                authorities
        );
    }
}
