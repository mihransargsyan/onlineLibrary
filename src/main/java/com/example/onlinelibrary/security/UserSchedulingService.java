package com.example.onlinelibrary.security;

import com.example.onlinelibrary.entity.User;
import com.example.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSchedulingService {

    private final UserService userService;

    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void removeExpiredTokens() {
        List<User> all = userService.findAll();
        for (User user : all) {
            LocalDateTime tokenCreatedDate = user.getTokenCreatedDate();
            if (tokenCreatedDate != null) {
                LocalDateTime expireDateTime = tokenCreatedDate.plusDays(1);
                if (StringUtils.isNotEmpty(user.getToken())
                        && LocalDateTime.now().isAfter(expireDateTime)) {
                    user.setToken(null);
                    user.setTokenCreatedDate(null);
                    userService.save(user);
                }
            }

        }
    }

}
