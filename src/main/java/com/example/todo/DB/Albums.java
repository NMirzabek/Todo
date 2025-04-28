package com.example.springdb.DB;

import com.example.springdb.entity.Album;
import com.example.springdb.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.util.List;

public interface Albums {

    static List<Album> getAlbums(EntityManagerFactory emf, int users_id) {

        try (EntityManager em = emf.createEntityManager();){
            em.getTransaction().begin();
            Query query = em.createQuery("select a from Album a where a.user.id = :users_id");
            query.setParameter("users_id", users_id);
            List<Album> albums = query.getResultList();
            em.getTransaction().commit();
            return albums;
        }
    }

    static void addAlbum(EntityManagerFactory emf, Album album) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User managedUser = em.find(User.class, album.getUser().getId());
            album.setUser(managedUser);
            em.persist(album);
            em.getTransaction().commit();
        }
    }

    static Album deleteAlbumById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete from Photo p where p.album.id=:album_id").setParameter("album_id", id).executeUpdate();
            Album album = em.find(Album.class, id);
            em.remove(album);
            em.getTransaction().commit();
            return album;
        }
    }

    static Album getAlbumById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Album album = em.find(Album.class, id);
            em.getTransaction().commit();
            return album;
        }
    }

    static Album changeCurrentUser(EntityManagerFactory emf, Album album, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Album album1 = getAlbumById(emf, id);
            album1.setTitle(album.getTitle());
            em.merge(album1);
            em.getTransaction().commit();
            return album1;
        }
    }

}
