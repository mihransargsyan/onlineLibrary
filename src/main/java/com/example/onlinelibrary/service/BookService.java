package com.example.onlinelibrary.service;


import com.example.onlinelibrary.entity.*;
import com.example.onlinelibrary.repository.AuthorRepository;
import com.example.onlinelibrary.repository.BookImageRepository;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final BookImageRepository bookImageRepository;

    @Value("${books.upload.pathBookImages}")
    private String imagePath;

    @Value("${books.upload.pathBookPdf}")
    private String pdfPath;

    public Book addBook(Book book, MultipartFile[] uploadedFiles,MultipartFile uploadedPdf, User user, List<Integer> categories, List<Integer> authors) throws IOException {
        List<Category> categoriesFromDB = getCategoriesFromRequest(categories);
        List<Author> authorsFromDB = getAuthorsFromRequest(authors);
        book.setUser(user);
        book.setCategories(categoriesFromDB);
        book.setAuthors(authorsFromDB);
        save(book);
        saveBookImages(uploadedFiles, book);
        saveBookPdf(uploadedPdf, book);
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

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAllByUser(User user) {
        return bookRepository.findAllByUser(user);
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
        for (Integer authorId : authorsIds) {
            authors.add(authorRepository.getById(authorId));
        }
        return authors;
    }

        private void saveBookImages(MultipartFile[] uploadedFiles, Book book) throws IOException {
        if (uploadedFiles.length != 0) {
            for (MultipartFile uploadedFile : uploadedFiles) {
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
                File newFile = new File(imagePath + fileName);
                uploadedFile.transferTo(newFile);
                BookImage bookImage = BookImage.builder()
                        .name(fileName)
                        .book(book)
                        .build();
                bookImageRepository.save(bookImage);
            }
        }
    }

    private void saveBookPdf(MultipartFile uploadedPdf, Book book) {
        if (!uploadedPdf.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + uploadedPdf.getOriginalFilename();
            File newFile = new File(pdfPath + fileName);
            try {
                uploadedPdf.transferTo(newFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            book.setBookPdf(fileName);
            bookRepository.save(book);
        }
    }

}
