package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Role;
import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.security.CurrentUser;
import com.example.onlinelibrary.service.MailService;
import com.example.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/user/add")
    public String addUserPage() {
        return "saveUser";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute User user,
                          @AuthenticationPrincipal CurrentUser currentUser,
                          Locale locale) throws MessagingException {
        if (currentUser == null) {
            user.setActive(false);
            user.setToken(UUID.randomUUID().toString());
            user.setTokenCreatedDate(LocalDateTime.now());
            String encode = passwordEncoder.encode(user.getPassword());
            user.setPassword(encode);
            user.setRole(Role.USER);
            userService.save(user);
            mailService.sendHtmlEmail(user.getEmail(),
                    "Welcome " + user.getSurname(),
                    user, " http://localhost:8080/user/activate?token=" + user.getToken(),
                    "verifyTemplate", locale);
        } else {
            user.setRole(Role.USER);
            user.setActive(true);
            userService.save(user);
        }
        return "redirect:/";
    }

    @GetMapping("/user/activate")
    public String activateUser(ModelMap map, @RequestParam String token) {
        Optional<User> user = userService.findByToken(token);
        if (!user.isPresent()) {
            map.addAttribute("message", "User Does not exists");
            return "activateUser";
        }
        User userFromDb = user.get();
        if (userFromDb.isActive()) {
            map.addAttribute("message", "User already active!");
            return "activateUser";
        }
        userFromDb.setActive(true);
        userFromDb.setToken(null);
        userFromDb.setTokenCreatedDate(null);
        userService.save(userFromDb);
        map.addAttribute("message", "User activated, please login!");
        return "activateUser";
    }

    @GetMapping("/user/edit/{id}")
    public String editUserPage(ModelMap map,
                               @PathVariable("id") int id,
                               @AuthenticationPrincipal CurrentUser currentUser) {
        User user = userService.findById(id);
        if (user.getId() == currentUser.getUser().getId()) {
            map.addAttribute("user", user);
            return "saveUser";
        }
        return "redirect:/";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id,
                             @AuthenticationPrincipal CurrentUser currentUser) {
        User user = userService.findById(id);
        if (user.getId() == currentUser.getUser().getId()) {
            userService.deleteById(id);
        }
        return "redirect:/";
    }

}
