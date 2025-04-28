package com.example.springdb.servlets;

import com.example.springdb.DB.Comments;
import com.example.springdb.DB.Posts;
import com.example.springdb.entity.Comment;
import com.example.springdb.entity.Post;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentServlet {
    private final EntityManagerFactory emf;

    @PreAuthorize("hasAuthority('SHOW_COMMENT')")
    @GetMapping("/comments/{post_id}")
    public String getComments(@PathVariable int post_id, Model model) {
        List<Comment> comments = Comments.getAllCommentsByPostId(emf, post_id);
        Post post = Posts.getPostById(emf, post_id);
        model.addAttribute("user_id", post.getUser().getId());
        model.addAttribute("post_id", post_id);
        model.addAttribute("comments", comments);
        return "showCommentPage";
    }

    @PreAuthorize("hasAuthority('CREATE_COMMENT')")
    @GetMapping("/comment/add/{postId}")
    public String addCommentForm(@PathVariable int postId, Model model) {
        model.addAttribute("post_id", postId);
        return "addCommentPage";
    }

    @PreAuthorize("hasAuthority('CREATE_COMMENT')")
    @PostMapping("/comment/add/process/{postId}")
    public String addComment(@PathVariable int postId, @ModelAttribute Comment comment) {
        Post post = Posts.getPostById(emf, postId);
        comment.setPost(post);
        Comments.addComment(emf, comment);
        return "redirect:/comments/" + postId;
    }

    @PreAuthorize("hasAuthority('DELETE_COMMENT')")
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable int id) {
        Comment comment = Comments.findById(emf, id);
        Comments.deleteById(emf, id);
        return "redirect:/comments/" + comment.getPost().getId();
    }

    @PreAuthorize("hasAuthority('UPDATE_COMMENT')")
    @GetMapping("/comment/update/{id}")
    public String updateCommentForm(@PathVariable int id, Model model) {
        Comment comment = Comments.findById(emf, id);
        model.addAttribute("comment", comment);
        return "updateCommentPage";
    }

    @PreAuthorize("hasAuthority('UPDATE_COMMENT')")
    @PostMapping("/comment/update/process/{id}")
    public String updateComment(@PathVariable int id, @ModelAttribute Comment comment) {
        Comment existing = Comments.findById(emf, id);
        existing.setName(comment.getName());
        existing.setEmail(comment.getEmail());
        existing.setBody(comment.getBody());
        Comments.changeComment(emf, existing);
        return "redirect:/comments/" + existing.getPost().getId();
    }
}
