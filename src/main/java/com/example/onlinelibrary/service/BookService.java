package com.example.onlinelibrary.service;


import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.entity.Category;
import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.repository.AuthorRepository;
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

    @Value("${books.upload.path}")
    private String imagePath;

    public Book addBook(Book book, MultipartFile[] uploadedFiles, User user, List<Integer> categories, List<Integer> authors) throws IOException {
        List<Category> categoriesFromDB = getCategoriesFromRequest(categories);
        List<Author> authorsFromDB = getAuthorsFromRequest(authors);
        book.setUser(user);
        book.setCategories(categoriesFromDB);
        book.setAuthors(authorsFromDB);
        bookRepository.save(book);
     //   saveBookImages(uploadedFiles, book);
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

//    private void saveBookImages(MultipartFile[] uploadedFiles, Book book) throws IOException {
//        if (uploadedFiles.length != 0) {
//            for (MultipartFile uploadedFile : uploadedFiles) {
//                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
//                File newFile = new File(imagePath + fileName);
//                uploadedFile.transferTo(newFile);
//                ItemImage itemImage = ItemImage.builder()
//                        .name(fileName)
//                        .item(item)
//                        .build();
//
//                itemImageRepository.save(itemImage);
//            }
//
//        }
//    }

    private List<Author> getAuthorsFromRequest(List<Integer> authorsIds) {
        List<Author> authors = new ArrayList<>();
        for (Integer author : authorsIds) {
            authors.add(authorRepository.getById(author));
        }
        return authors;
    }

}
