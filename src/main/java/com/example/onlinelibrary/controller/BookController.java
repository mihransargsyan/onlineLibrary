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
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
        List<Category> categories = categoryService.findAll();
        map.addAttribute("categories", categories);
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
                          @RequestParam("pictures") MultipartFile[] uploadedImages,
                          @RequestParam("pdf") MultipartFile uploadedPdf,
                          @AuthenticationPrincipal CurrentUser currentUser,
                          ModelMap map) {

        Book book = mapper.map(createBookRequest, Book.class);

        List<String> errorMsg = new ArrayList<>();
        if (book.getTitle() == null || book.getTitle().equals("")) {
            errorMsg.add("title is required");
        }
        if (book.getEdition() == null || book.getEdition().equals("")) {
            errorMsg.add("edition is required");
        }
        if (book.getDescription() == null || book.getDescription().equals("")) {
            errorMsg.add("description is required");
        }
        if(!uploadedPdf.getOriginalFilename().endsWith(".pdf")){
            errorMsg.add("required is PDF file");
        }

        if (uploadedPdf.isEmpty() || uploadedPdf.getSize() == 0 ) {
            errorMsg.add("Book PDF is required");
        }

        if (!errorMsg.isEmpty()) {
            map.addAttribute("errors", errorMsg);
            User user = currentUser.getUser();
            map.addAttribute("categories", categoryService.findAll());
            map.addAttribute("authors", authorService.findAll());
            map.addAttribute("user", user);
            return "saveBook";
        }
        try {
            bookService.addBook(book, uploadedImages, uploadedPdf, currentUser.getUser(), createBookRequest.getCategories(), createBookRequest.getAuthors());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/books";
    }

    @PostMapping("/books/search")
    public String searchBook(ModelMap map,
                             @RequestParam("keyword") String keyword) {
        List<Book> books = bookService.findBooksByTitle(keyword);
            map.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/category/{id}")
    public String categoryIdBook(@PathVariable int id, ModelMap map) {
        List<Book> books= new ArrayList<>();
        List<Book> allBooks = bookService.findAll();
        for (Book book : allBooks) {
            for (Category category : book.getCategories()) {
                if (category.getId() == id) {
                  books.add(book);
                }
            }
        }
        List<Category> categories = categoryService.findAll();
        map.addAttribute("categories", categories);
        map.addAttribute("books", books);
        return "books";
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
