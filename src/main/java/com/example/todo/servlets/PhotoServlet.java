package com.example.springdb.servlets;

import com.example.springdb.DB.Albums;
import com.example.springdb.DB.Photos;
import com.example.springdb.entity.Album;
import com.example.springdb.entity.Photo;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PhotoServlet {
    private final EntityManagerFactory emf;

    // SHOW PHOTOS IN AN ALBUM
    @PreAuthorize("hasAuthority('SHOW_PHOTO')")
    @GetMapping("/photo/{album_id}")
    public String photo(@PathVariable int album_id, Model model) {
        List<Photo> photos = Photos.getPhotosByAlbumId(emf, album_id);
        Album album = Albums.getAlbumById(emf, album_id);
        model.addAttribute("user_id", album.getUser().getId());
        model.addAttribute("album_id", album_id);
        model.addAttribute("photos", photos);
        return "showPhotoPage";
    }

    // SHOW ADD PHOTO FORM
    @PreAuthorize("hasAuthority('CREATE_PHOTO')")
    @GetMapping("/photo/add/{album_id}")
    public String showAddPhoto(@PathVariable int album_id, Model model) {
        model.addAttribute("album_id", album_id);
        return "addPhotoPage";
    }

    // PROCESS NEW PHOTO
    @PreAuthorize("hasAuthority('CREATE_PHOTO')")
    @PostMapping("/photo/add/{album_id}")
    public String processAddPhoto(@PathVariable int album_id, @ModelAttribute Photo photo) throws IOException {
        Album album = Albums.getAlbumById(emf, album_id);
        photo.setAlbum(album);
        Photos.addPhoto(emf, photo);
        return "redirect:/photo/" + album_id;
    }

    // DELETE A PHOTO
    @PreAuthorize("hasAuthority('DELETE_PHOTO')")
    @PostMapping("/photo/delete/{photo_id}")
    public String deletePhoto(@PathVariable int photo_id) {
        Photo photo = Photos.getPhotoById(emf, photo_id);
        Photos.deletePhotoById(emf, photo_id);
        return "redirect:/photo/" + photo.getAlbum().getId();
    }

    // SHOW UPDATE PHOTO FORM
    @PreAuthorize("hasAuthority('UPDATE_PHOTO')")
    @GetMapping("/photo/update/{photo_id}")
    public String showUpdatePhoto(@PathVariable int photo_id, Model model) {
        Photo photo = Photos.getPhotoById(emf, photo_id);
        model.addAttribute("photo", photo);
        return "updatePhotoPage";
    }

    // PROCESS PHOTO UPDATE
    @PreAuthorize("hasAuthority('UPDATE_PHOTO')")
    @PostMapping("/photo/update/process/{photo_id}")
    public String processUpdatePhoto(@PathVariable int photo_id, @ModelAttribute Photo photo) throws IOException {
        Photo updated = Photos.changedPhoto(emf, photo_id, photo);
        return "redirect:/photo/" + updated.getAlbum().getId();
    }
}
