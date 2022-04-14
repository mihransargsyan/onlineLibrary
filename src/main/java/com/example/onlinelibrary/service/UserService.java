package com.example.onlinelibrary.service;


import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
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

}
