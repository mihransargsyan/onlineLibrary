package com.example.onlinelibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String edition;

    @ManyToMany
    private List<Category> categories;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Author> authors;

    @OneToMany(mappedBy = "book")
    private List<BookPicture> bookPictures;

    private String bookPdf;

}
