package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Error. No user found with username: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true,
                getAuthorities(user.isAdmin()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(boolean isAdmin) {
        String role = isAdmin ? "ADMIN" : "USER";
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
