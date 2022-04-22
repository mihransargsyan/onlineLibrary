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
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    private  UserDetailsImpl userDetails;

    @Autowired
    private   PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .and().logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET,"/user/add").permitAll()
                .antMatchers(HttpMethod.POST,"/user/add").permitAll()
                .antMatchers(HttpMethod.GET,"/user/activate").permitAll()
                .antMatchers(HttpMethod.GET,"/user/edit/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/user/delete/{id}").hasAnyAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/books").permitAll()
                .antMatchers(HttpMethod.POST,"/books").permitAll()
                .antMatchers(HttpMethod.GET,"/books/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/books/{id}").permitAll()
                .antMatchers("/books/add").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .anyRequest().permitAll();
           //     .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder);
    }

}
