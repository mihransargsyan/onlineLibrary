package com.example.onlinelibrary.config;

import com.example.onlinelibrary.entity.Role;
import com.example.onlinelibrary.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsImpl userDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .and().logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user/edit/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/user/delete/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/books/add").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/myBooks").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/books/byUser/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/books/delete/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/author/add").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/book/download").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder);
    }


}
