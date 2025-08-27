package com.neoutilix.auth_jwt_springboot.service.impl;

import com.neoutilix.auth_jwt_springboot.dto.RegisterRequest;
import com.neoutilix.auth_jwt_springboot.dto.UserDto;
import com.neoutilix.auth_jwt_springboot.entity.Role;
import com.neoutilix.auth_jwt_springboot.entity.User;
import com.neoutilix.auth_jwt_springboot.mapper.UserMapper;
import com.neoutilix.auth_jwt_springboot.repository.RoleRepository;
import com.neoutilix.auth_jwt_springboot.repository.UserRepository;
import com.neoutilix.auth_jwt_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(RegisterRequest request, boolean admin) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        user.getRoles().add(userRole);
        if (admin) {
            user.getRoles().add(adminRole);
        }
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserDto getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        String email = auth.getName();
        return UserMapper.toDto(findByEmail(email));
    }
}
