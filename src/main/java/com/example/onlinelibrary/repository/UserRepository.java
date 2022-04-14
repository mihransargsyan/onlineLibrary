package com.example.onlinelibrary.repository;

import com.example.onlinelibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
