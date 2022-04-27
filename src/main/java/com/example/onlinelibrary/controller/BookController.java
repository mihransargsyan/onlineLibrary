package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.dto.CreateBookRequest;
import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.entity.Category;
import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.security.CurrentUser;
import com.example.onlinelibrary.service.AuthorService;
import com.example.onlinelibrary.service.BookService;
import com.example.onlinelibrary.service.CategoryService;
import com.example.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        map.addAttribute("authors", authorService.findAll());
        map.addAttribute("user", user);
        return "saveBook";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute CreateBookRequest createBookRequest,
                          @RequestParam("pictures") MultipartFile[] uploadedFiles,
                          @RequestParam("pdf") MultipartFile uploadedPdf,
                          @AuthenticationPrincipal CurrentUser currentUser) {
        Book book = mapper.map(createBookRequest, Book.class);
        try {
            bookService.addBook(book, uploadedFiles, uploadedPdf, currentUser.getUser(), createBookRequest.getCategories(), createBookRequest.getAuthors());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/books";
    }

    @GetMapping("/books/category/{id}")
    public String categoryIdBook(@PathVariable int id, ModelMap map) {

    List<Book> books = bookService.findAll();
        for (Book book : books) {
            List<Category> categories = book.getCategories();
            for (Category category : categories) {
                if(category.getId()==id){

                }
            }
        }
        List<Category> categories = categoryService.findAll();
        map.addAttribute("categories", categories);
        return "main";
    }

    @GetMapping("/books/{id}")
    public String singleBook(@PathVariable int id, ModelMap map) {
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("book", bookService.findById(id));
        return "singleBook";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") int id,
                             @AuthenticationPrincipal CurrentUser currentUser) {
        Book book = bookService.findById(id);
        if (book.getUser().getId() == currentUser.getUser().getId()) {
            bookService.deleteById(id);
        }
        return "redirect:/books";
    }

}
