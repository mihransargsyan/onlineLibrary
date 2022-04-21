package com.example.onlinelibrary.dto;

import com.example.onlinelibrary.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

    private int id;
    private String title;
    private String description;
    private String edition;
    private List<Integer> categories;
    private List<Integer> authors;


}
