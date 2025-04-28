package com.example.springdb.servlets;

import com.example.springdb.DB.Todo;
import com.example.springdb.DB.Users;
import com.example.springdb.entity.ToDos;
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
public class TodoServlet {
    private final EntityManagerFactory emf;

    @PreAuthorize("hasAuthority('SHOW_TO_DO')")
    @GetMapping("/todo/{id}")
    public String show(@PathVariable int id, Model model) {
        List<ToDos> toDos = Todo.getTodosByUserId(emf, id);
        model.addAttribute("user_id", id);
        model.addAttribute("todos", toDos);
        return "showTodosPage";
    }

    @PreAuthorize("hasAuthority('CREATE_TO_DO')")
    @GetMapping("/todos/add/{userId}")
    public String showAdd(@PathVariable int userId, Model model) {
        model.addAttribute("user_id", userId);
        return "addTodoPage";
    }

    @PreAuthorize("hasAuthority('CREATE_TO_DO')")
    @PostMapping("/todos/add/process/{userId}")
    public String processAdd(
            @PathVariable int userId,
            @ModelAttribute ToDos toDos
    ) {
        User user = Users.getUserById(emf, userId);
        toDos.setUser(user);
        Todo.addTodo(emf, toDos);
        return "redirect:/todo/" + user.getId();
    }

    @PreAuthorize("hasAuthority('DELETE_TO_DO')")
    @PostMapping("/todos/delete/{id}")
    public String delete(@PathVariable int id) {
        ToDos toDos = Todo.deleteById(emf, id);
        return "redirect:/todo/" + toDos.getUser().getId();
    }

    @PreAuthorize("hasAuthority('UPDATE_TO_DO')")
    @GetMapping("/todos/update/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        ToDos toDos = Todo.getById(emf, id);
        model.addAttribute("todos", toDos);
        return "updateTodosPage";
    }

    @PreAuthorize("hasAuthority('UPDATE_TO_DO')")
    @PostMapping("/todos/update/process/{user_id}")
    public String updateToDo(
            @PathVariable("user_id") int userId,
            @ModelAttribute ToDos toDos
    ) {
        ToDos existingToDo = Todo.getById(emf, toDos.getId());
        existingToDo.setTitle(toDos.getTitle());
        existingToDo.setCompleted(toDos.isCompleted());
        Todo.update(emf, existingToDo);
        return "redirect:/todo/" + userId;
    }
}
