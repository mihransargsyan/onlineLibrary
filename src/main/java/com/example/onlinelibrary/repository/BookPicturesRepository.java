package com.example.onlinelibrary.repository;


import com.example.onlinelibrary.entity.BookPicture;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookPicturesRepository extends JpaRepository<BookPicture, Integer> {

}
