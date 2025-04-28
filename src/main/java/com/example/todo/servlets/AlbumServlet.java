package com.example.springdb.servlets;

import com.example.springdb.DB.Albums;
import com.example.springdb.DB.Users;
import com.example.springdb.entity.Album;
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
public class AlbumServlet {
    private final EntityManagerFactory emf;

    @PreAuthorize("hasAuthority('SHOW_ALBUM')")
    @GetMapping("/album/{userId}")
    public String listAlbums(@PathVariable("userId") int userId, Model model) {
        List<Album> albums = Albums.getAlbums(emf, userId);
        model.addAttribute("user_id", userId);
        model.addAttribute("albums", albums);
        return "showAlbumPage";
    }

    @PreAuthorize("hasAuthority('CREATE_ALBUM')")
    @GetMapping("/album/add/{userId}")
    public String showAddForm(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("user_id", userId);
        return "addAlbumPage";
    }

    @PreAuthorize("hasAuthority('CREATE_ALBUM')")
    @PostMapping("/album/add/{userId}")
    public String processAdd(@PathVariable("userId") int userId, @ModelAttribute Album album) {
        User user = Users.getUserById(emf, userId);
        album.setUser(user);
        Albums.addAlbum(emf, album);
        return "redirect:/album/" + userId;
    }

    @PreAuthorize("hasAuthority('DELETE_ALBUM')")
    @PostMapping("/album/delete/{albumId}")
    public String deleteAlbum(@PathVariable("albumId") int albumId) {
        Album album = Albums.deleteAlbumById(emf, albumId);
        return "redirect:/album/" + album.getUser().getId();
    }

    @PreAuthorize("hasAuthority('UPDATE_ALBUM')")
    @GetMapping("/album/update/{albumId}")
    public String showUpdateForm(@PathVariable("albumId") int albumId, Model model) {
        Album album = Albums.getAlbumById(emf, albumId);
        model.addAttribute("album", album);
        return "updateAlbumPage";
    }

    @PreAuthorize("hasAuthority('UPDATE_ALBUM')")
    @PostMapping("/album/update/{albumId}")
    public String processUpdate(@PathVariable("albumId") int albumId, @ModelAttribute Album album) {
        Album updated = Albums.changeCurrentUser(emf, album, albumId);
        return "redirect:/album/" + updated.getUser().getId();
    }
}
