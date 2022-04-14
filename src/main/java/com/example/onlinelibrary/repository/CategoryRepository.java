package com.example.onlinelibrary.repository;

import com.example.onlinelibrary.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
