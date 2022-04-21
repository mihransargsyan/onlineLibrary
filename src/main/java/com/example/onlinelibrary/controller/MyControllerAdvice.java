package com.example.onlinelibrary.controller;


import com.example.onlinelibrary.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MyControllerAdvice {

    @ModelAttribute
    public CurrentUser currentUser(@AuthenticationPrincipal CurrentUser currentUser){
        return currentUser;
    }

}
