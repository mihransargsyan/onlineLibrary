package com.example.onlinelibrary.service;


import com.example.onlinelibrary.entity.Role;
import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    public User save(User user) {
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public User findById(int id) {
        return userRepository.getById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

}
