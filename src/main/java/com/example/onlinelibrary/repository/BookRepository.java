package com.example.onlinelibrary.repository;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByUser(User user);

    List<Book> findAllByTitle(String keyword);

}
