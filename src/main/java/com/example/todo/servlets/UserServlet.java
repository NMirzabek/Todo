package com.example.springdb.servlets;

import com.example.springdb.DB.Users;
import com.example.springdb.entity.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserServlet {
    private final EntityManagerFactory emf;

    @PreAuthorize("hasAuthority('SHOW_USER')")
    @GetMapping("/user")
    public String users(Model model) {
        List<User> users = Users.getUsers(emf);
        model.addAttribute("users", users);
        return "showUserPage";
    }

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @GetMapping("/user/add")
    public String addUser() {
        return "addUserPage";
    }

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping("/user/add/process")
    public String processUser(@ModelAttribute("user") User user) {
        Users.addUser(emf, user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        Users.deleteUserById(emf, id);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @GetMapping("/user/update/{id}")
    public String updateUserForm(@PathVariable int id, Model model) {
        User user = Users.getUserById(emf, id);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/user/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable int id) {
        Users.changeUser(emf, id, user);
        return "redirect:/user";
    }
}
