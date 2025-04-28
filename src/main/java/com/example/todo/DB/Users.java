package com.example.springdb.DB;

import com.example.springdb.entity.Album;
import com.example.springdb.entity.Post;
import com.example.springdb.entity.User;
import jakarta.persistence.*;

import java.util.List;

public interface Users {

    static List<User> getUsers(EntityManagerFactory emf) {

        try (EntityManager em = emf.createEntityManager();){
            em.getTransaction().begin();
            Query query = em.createQuery("select u from users u");
            List<User> users = query.getResultList();
            em.getTransaction().commit();
            return users;
        }
    }

    static void addUser(EntityManagerFactory emf, User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }

    static void deleteUserById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM ToDos t WHERE t.user.id = :userId")
                    .setParameter("userId", id)
                    .executeUpdate();
            List<Album> albums = Albums.getAlbums(emf, id);
            for (Album album : albums) {
                Albums.deleteAlbumById(emf, album.getId());
            }
            em.createQuery("DELETE FROM Album a WHERE a.user.id = :userId")
                    .setParameter("userId", id)
                    .executeUpdate();
            List<Post> postsByUserId = Posts.getPostsByUserId(emf, id);
            for (Post post : postsByUserId) {
                Posts.deletePostById(emf, post.getId());
            }
            em.createQuery("DELETE FROM Post p WHERE p.user.id = :userId")
                    .setParameter("userId", id)
                    .executeUpdate();
            em.createQuery("DELETE FROM users u WHERE u.id = :userId")
                    .setParameter("userId", id)
                    .executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user and associated data", e);
        }
    }

    static User getUserById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            em.getTransaction().commit();
            return user;
        }
    }

    static void changeUser(EntityManagerFactory emf, int id, User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<User> query = em.createQuery("select u from users u where u.id = :id", User.class);
            query.setParameter("id", id);
            User singleResult = query.getSingleResult();
            singleResult.setUsername(user.getUsername());
            singleResult.setEmail(user.getEmail());
            singleResult.setPhone(user.getPhone());
            singleResult.setWebsite(user.getWebsite());
            em.merge(singleResult);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(user);
        }
    }
}
