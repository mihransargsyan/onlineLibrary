package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.entity.Category;
import com.example.onlinelibrary.service.BookService;
import com.example.onlinelibrary.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${books.upload.pathBookImages}")
    private String imagePath;
    @Value("${books.upload.pathBookPdf}")
    private String pdfPath;

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String main(ModelMap map,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "12") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Book> userPage = bookService.findAll(pageRequest);
        map.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers", pageNumbers);
        }

       List<Category> categories = categoryService.findAll();
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

    @GetMapping(value = "/book/download",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    byte[] getPdf(@RequestParam("getPdf") String pdfName) throws IOException {
        InputStream inputStream = new FileInputStream(pdfPath + pdfName);
        return IOUtils.toByteArray(inputStream);
    }

}
