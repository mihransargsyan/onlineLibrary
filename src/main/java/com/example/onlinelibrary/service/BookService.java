package com.example.onlinelibrary.service;


import com.example.onlinelibrary.entity.*;
import com.example.onlinelibrary.repository.AuthorRepository;
import com.example.onlinelibrary.repository.BookPicturesRepository;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookPicturesRepository bookPicturesRepository;

    @Value("${books.upload.path}")
    private String imagePath;

    public Book addBook(Book book, MultipartFile[] uploadedFiles, User user, List<Integer> categories, List<Integer> authors) throws IOException {
        List<Category> categoriesFromDB = getCategoriesFromRequest(categories);
        List<Author> authorsFromDB = getAuthorsFromRequest(authors);
        book.setUser(user);
        book.setCategories(categoriesFromDB);
        book.setAuthors(authorsFromDB);
        bookRepository.save(book);
        saveBookImages(uploadedFiles, book);
        return book;
    }

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

    public List<Book> findAllByUser(User user) {
        return bookRepository.findAllByUser(user);
    }

    public Book findBookByUserId(int id){
        return bookRepository.findBookByUserId(id);
    }

    private List<Category> getCategoriesFromRequest(List<Integer> categoriesIds) {
        List<Category> categories = new ArrayList<>();
        for (Integer category : categoriesIds) {
            categories.add(categoryRepository.getById(category));
        }
        return categories;
    }


    private List<Author> getAuthorsFromRequest(List<Integer> authorsIds) {
        List<Author> authors = new ArrayList<>();
        for (Integer author : authorsIds) {
            authors.add(authorRepository.getById(author));
        }
        return authors;
    }

        private void saveBookImages(MultipartFile[] uploadedFiles, Book book) throws IOException {
        if (uploadedFiles.length != 0) {
            for (MultipartFile uploadedFile : uploadedFiles) {
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
                File newFile = new File(imagePath + fileName);
                uploadedFile.transferTo(newFile);
                BookPicture bookPicture = BookPicture.builder()
                        .name(fileName)
                        .book(book)
                        .build();

                bookPicturesRepository.save(bookPicture);
            }

        }
    }

}
