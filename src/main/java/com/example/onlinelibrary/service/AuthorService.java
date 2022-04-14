package com.example.onlinelibrary.service;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public void deleteById(int id) {
        authorRepository.deleteById(id);
    }

    public Author findById(int id) {
        return authorRepository.getById(id);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

}
