package com.example.springdb.servlets;

import com.example.springdb.DB.PermissionDB;
import com.example.springdb.DB.RoleDB;
import com.example.springdb.entity.Permission;
import com.example.springdb.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AddRole {
    private final PermissionDB permissionDB;
    private final RoleDB roleDB;
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/role/add")
    public String addRole(Model model) {
        List<Permission> permissions = permissionDB.findAll();
        model.addAttribute("permissions", permissions);
        return "addRole";
    }
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/manage/json/roles/create")
    public String createRole(@RequestParam("name") String name, @RequestParam("permissions") List<Integer> permissionIds) {
        Role role = new Role();
        role.setName(name.toUpperCase());
        List<Permission> selectedPermissions = permissionDB.findAllById(permissionIds);
        role.setPermissions(selectedPermissions);
        roleDB.save(role);
        return "redirect:/manage/json/users";
    }
}
