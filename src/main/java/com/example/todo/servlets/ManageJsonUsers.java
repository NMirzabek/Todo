package com.example.springdb.servlets;

import com.example.springdb.DB.JsonUserDB;
import com.example.springdb.DB.RoleDB;
import com.example.springdb.entity.JsonUser;
import com.example.springdb.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.Context;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ManageJsonUsers {
    private final JsonUserDB jsonUserDB;
    private final RoleDB roleDB;
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/manage/json/users")
    public String manageJsonUsers(Model model) {
        JsonUser user = (JsonUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JsonUser> users = jsonUserDB.findByIdNot(user.getId());
        model.addAttribute("users", users);
        return "manage";
    }
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/manage/json/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        jsonUserDB.deleteById(id);
        return "redirect:/manage/json/users";
    }
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/manage/json/users/edit/{id}")
    public String editUser(@PathVariable int id, Model model) {
        Optional<JsonUser> byId = jsonUserDB.findById(id);
        if (byId.isPresent()) {
            List<Role> roles = roleDB.findAll();
            JsonUser jsonUser = byId.get();
            model.addAttribute("user", jsonUser);
            model.addAttribute("roles", roles);
            return "edit";
        }
        return "redirect:/manage/json/users";
    }
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/manage/json/users/edit/{id}")
    public String editUser(@PathVariable int id, @RequestParam("roleIds") List<Integer> roleIds) {
        Optional<JsonUser> userOpt = jsonUserDB.findById(id);
        if (userOpt.isPresent()) {
            JsonUser jsonUser = userOpt.get();
            List<Role> roles = roleDB.findAllById(roleIds);
            jsonUser.setRoles(roles);
            jsonUserDB.save(jsonUser);
            return "redirect:/manage/json/users";
        } else {
            return "redirect:/manage/json/users?error=userNotFound";
        }
    }


}
