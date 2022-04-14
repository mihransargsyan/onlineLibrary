package com.example.onlinelibrary.service;

import com.example.onlinelibrary.entity.Category;
import com.example.onlinelibrary.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(int id) {
        return categoryRepository.getById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
