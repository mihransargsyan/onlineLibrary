package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.security.CurrentUser;
import com.example.onlinelibrary.service.AuthorService;
import com.example.onlinelibrary.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final CategoryService categoryService;

    @GetMapping("/author/add")
    public String addAuthorPage(Model map) {
        return "saveAuthor";
    }

    @PostMapping("/author/add")
    public String addAuthor(@RequestParam("dateOfBirth") String data,
                            @RequestParam("name") String name,
                            @RequestParam("surname") String surname,
                            ModelMap map,  @AuthenticationPrincipal CurrentUser currentUser) {
        Author author = new Author();
        List<String> errorMsg= new ArrayList<>();
        if (name == null || name.equals("")) {
            errorMsg.add("name is required");
        }
        if (surname == null || surname.equals("")) {
            errorMsg.add("surname is required");
        }
        if (data == null || data.equals("")) {
            errorMsg.add("data of birth is required");
        }
        if (!errorMsg.isEmpty()) {
            map.addAttribute("errors", errorMsg);
            return "saveAuthor";
        }
        author.setName(name);
        author.setSurname(surname);
        author.setDateOfBirth(LocalDate.parse(data));
        authorService.save(author);
        User user = currentUser.getUser();
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("authors", authorService.findAll());
        map.addAttribute("user", user);
        return "saveBook";
    }

}
