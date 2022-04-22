package com.example.onlinelibrary.repository;


import com.example.onlinelibrary.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookImageRepository extends JpaRepository<BookImage, Integer> {


}
