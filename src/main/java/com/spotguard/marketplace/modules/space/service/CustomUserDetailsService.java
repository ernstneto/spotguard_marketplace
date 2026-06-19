package com.spotguard.marketplace.modules.space.service;

import com.spotguard.marketplace.modules.space.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.spotguard.marketplace.modules.space.entity.User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("Usuário não encontrado: " + email));

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
