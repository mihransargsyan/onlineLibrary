package com.example.onlinelibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book_pictures")
public class BookPicture {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String name;

        @ManyToOne
        private Book book;

}
