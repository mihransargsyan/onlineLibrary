package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.entity.Category;
import com.example.onlinelibrary.service.BookService;
import com.example.onlinelibrary.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${books.upload.path}")
    private String imagePath;

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String main(ModelMap map) {
        List<Book> books = bookService.findAll();
        List<Category> categories = categoryService.findAll();
        map.addAttribute("books", books);
        map.addAttribute("categories", categories);
        return "main";
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        InputStream inputStream = new FileInputStream(imagePath + picName);
        return IOUtils.toByteArray(inputStream);
    }

}
