package com.example.onlinelibrary.service;


import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public Book findById(int id) {
        return bookRepository.getById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

}
