package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.dto.CreateBookRequest;
import com.example.onlinelibrary.entity.*;
import com.example.onlinelibrary.security.CurrentUser;
import com.example.onlinelibrary.service.AuthorService;
import com.example.onlinelibrary.service.BookService;
import com.example.onlinelibrary.service.CategoryService;
import com.example.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final ModelMapper mapper;


    @GetMapping("/books")
    public String booksPage(ModelMap map) {
        List<Book> books = bookService.findAll();
        map.addAttribute("books", books);

        return "books";
    }

    @GetMapping("/books/byUser/{id}")
    public String booksByUserPage(ModelMap map, @PathVariable("id") int id) {
        User user = userService.findById(id);
        List<Book> books = bookService.findAllByUser(user);
        map.addAttribute("books", books);
        return "books";
    }


    @GetMapping("/myBooks")
    public String myBooks(ModelMap map, @AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        List<Book> books = bookService.findAllByUser(user);
        map.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/add")
    public String addBookPage(ModelMap map, @AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("authors",authorService.findAll());
        map.addAttribute("user", user);
        return "saveBook";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute CreateBookRequest createBookRequest,
                          @RequestParam("pictures") MultipartFile[] uploadedFiles,
                          @AuthenticationPrincipal CurrentUser currentUser) {
        Book book = mapper.map(createBookRequest, Book.class);
        try {
            bookService.addBook(book,uploadedFiles,currentUser.getUser(),createBookRequest.getCategories(),createBookRequest.getAuthors());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String singleBook(@PathVariable int id, ModelMap map) {
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("book", bookService.findById(id));
        return "singleBook";
    }

}
