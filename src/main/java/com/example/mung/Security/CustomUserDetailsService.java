package com.example.mung.Security;

import com.example.mung.entity.UserEntity;
import com.example.mung.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserLoginId(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + id));

        return new org.springframework.security.core.userdetails.User(
                user.getUser_login_id(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().split(","))

        );
    }
}