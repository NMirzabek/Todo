package com.example.springdb.servlets;

import com.example.springdb.DB.JsonUserDB;
import com.example.springdb.entity.JsonUser;
import com.example.springdb.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class Register {
    private final PasswordEncoder passwordEncoder;
    private final JsonUserDB jsonUserDB;
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute JsonUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        jsonUserDB.save(user);
        return "redirect:/cabinet";
    }
}
