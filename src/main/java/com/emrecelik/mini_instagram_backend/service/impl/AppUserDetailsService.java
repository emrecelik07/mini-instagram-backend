package com.emrecelik.mini_instagram_backend.service.impl;

import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
    }
}
