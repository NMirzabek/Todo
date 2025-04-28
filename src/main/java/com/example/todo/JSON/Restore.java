 package com.example.springdb.JSON;

import com.example.springdb.DB.Users;
import com.example.springdb.DTO.*;
import com.example.springdb.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Restore implements Runnable {
    private final EntityManagerFactory emf;

    @Override
    public void run() {
        EntityManager em = emf.createEntityManager();
        List<User> current= Users.getUsers(emf);
        if (current.size()==0 || current==null) {
            em.getTransaction().begin();
            RestTemplate restTemplate = new RestTemplate();


            // USERS
            User[] users = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/users", User[].class);
            Map<Integer, User> userMap = new HashMap<>();
            for (User u : users) {
                int jsonId = u.getJsonId();
                u.setId(null);
                em.persist(u);
                em.flush();
                userMap.put(jsonId, u);
            }

            // ALBUMS
            AlbumDTO[] albumDTOs = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/albums", AlbumDTO[].class);
            Map<Integer, Album> albumMap = new HashMap<>();
            for (AlbumDTO dto : albumDTOs) {
                Album a = new Album();
                a.setTitle(trim(dto.getTitle()));
                a.setUser(userMap.get(dto.getUserId()));
                em.persist(a);
                em.flush();
                albumMap.put(dto.getId(), a);
            }

            // POSTS
            PostDTO[] postDTOs = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/posts", PostDTO[].class);
            Map<Integer, Post> postMap = new HashMap<>();
            for (PostDTO dto : postDTOs) {
                Post p = new Post();
                p.setTitle(trim(dto.getTitle()));
                p.setBody(trim(dto.getBody()));
                p.setUser(userMap.get(dto.getUserId()));
                em.persist(p);
                em.flush();
                postMap.put(dto.getId(), p);
            }

            // COMMENTS
            CommentDTO[] commentDTOs = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/comments", CommentDTO[].class);
            for (CommentDTO dto : commentDTOs) {
                Comment c = new Comment();
                c.setName(trim(dto.getName()));
                c.setEmail(trim(dto.getEmail()));
                c.setBody(trim(dto.getBody()));
                c.setPost(postMap.get(dto.getPostId()));
                em.persist(c);
            }

            // PHOTOS
            PhotoDTO[] photoDTOs = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/photos", PhotoDTO[].class);
            for (PhotoDTO dto : photoDTOs) {
                Photo ph = new Photo();
                ph.setTitle(trim(dto.getTitle()));
                ph.setUrl(trim(dto.getUrl()));
                ph.setThumbnailUrl(trim(dto.getThumbnailUrl()));
                ph.setAlbum(albumMap.get(dto.getAlbumId()));
                em.persist(ph);
            }

            // TODOS
            ToDosDTO[] todoDTOs = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/todos", ToDosDTO[].class);
            for (ToDosDTO dto : todoDTOs) {
                ToDos t = new ToDos();
                t.setTitle(trim(dto.getTitle()));
                t.setCompleted(dto.isCompleted());
                t.setUser(userMap.get(dto.getUserId()));
                em.persist(t);
            }

            em.getTransaction().commit();
            em.close();
        }
    }

    private String trim(String s) {
        return (s != null && s.length() > 255) ? s.substring(0,255) : s;
    }
}

