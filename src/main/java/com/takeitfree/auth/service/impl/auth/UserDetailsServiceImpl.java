package com.takeitfree.auth.service.impl.auth;

import com.takeitfree.auth.models.User;
import com.takeitfree.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username));

        if(user.isEmpty()) throw new UsernameNotFoundException("User not found");

        return user.get();
    }
}
