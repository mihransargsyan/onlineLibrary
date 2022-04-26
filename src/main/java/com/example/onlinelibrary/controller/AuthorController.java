package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/add")
    public String addAuthorPage(Model map) {
        map.addAttribute("authors", authorService.findAll());
        return "saveAuthor";
    }

    @PostMapping("/author/add")
    public String addAuthor( @RequestParam("dateOfBirth") String data,
                             @RequestParam("name") String name,
                             @RequestParam("surname") String surname) {
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        author.setDateOfBirth(LocalDate.parse(data));
        authorService.save(author);
        return "books";
    }
}
