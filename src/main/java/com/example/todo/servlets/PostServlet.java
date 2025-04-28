package com.example.springdb.servlets;

import com.example.springdb.DB.Posts;
import com.example.springdb.DB.Users;
import com.example.springdb.entity.Post;
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
public class PostServlet {
    private final EntityManagerFactory emf;

    // LIST POSTS FOR A USER (requires SHOW_POST permission)
    @PreAuthorize("hasAuthority('SHOW_POST')")
    @GetMapping("/post/{userId}")
    public String listPosts(@PathVariable("userId") int userId, Model model) {
        List<Post> posts = Posts.getPostsByUserId(emf, userId);
        model.addAttribute("user_id", userId);
        model.addAttribute("posts", posts);
        return "showPostsPage";
    }

    // SHOW CREATE FORM (requires CREATE_POST permission)
    @PreAuthorize("hasAuthority('CREATE_POST')")
    @GetMapping("/post/create/{userId}")
    public String showCreateForm(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("user_id", userId);
        return "addPostPage";
    }

    // PROCESS CREATION (requires CREATE_POST permission)
    @PreAuthorize("hasAuthority('CREATE_POST')")
    @PostMapping("/post/create/{userId}")
    public String processCreate(
            @PathVariable("userId") int userId,
            @ModelAttribute Post post
    ) {
        User user = Users.getUserById(emf, userId);
        post.setUser(user);
        Posts.addPost(emf, post);
        return "redirect:/post/" + userId;
    }

    // DELETE A POST (requires DELETE_POST permission)
    @PreAuthorize("hasAuthority('DELETE_POST')")
    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") int postId) {
        Post post = Posts.getPostById(emf, postId);
        Posts.deletePostById(emf, postId);
        return "redirect:/post/" + post.getUser().getId();
    }

    // SHOW UPDATE FORM (requires UPDATE_POST permission)
    @PreAuthorize("hasAuthority('UPDATE_POST')")
    @GetMapping("/post/update/{postId}")
    public String showUpdateForm(@PathVariable("postId") int postId, Model model) {
        Post post = Posts.getPostById(emf, postId);
        model.addAttribute("post", post);
        return "updatePostPage";
    }

    // PROCESS UPDATE (requires UPDATE_POST permission)
    @PreAuthorize("hasAuthority('UPDATE_POST')")
    @PostMapping("/post/update/process/{postId}")
    public String processUpdate(
            @PathVariable("postId") int postId,
            @ModelAttribute Post post
    ) {
        // ensure the correct user is set
        User user = Users.getUserById(emf, Posts.getPostById(emf, postId).getUser().getId());
        post.setUser(user);
        Post updated = Posts.changePost(emf, post);
        return "redirect:/post/" + updated.getUser().getId();
    }
}
