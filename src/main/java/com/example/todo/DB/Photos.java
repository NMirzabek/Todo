package com.example.springdb.DB;

import com.example.springdb.entity.Photo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public interface Photos {

    static List<Photo> getPhotosByAlbumId(EntityManagerFactory emf, int albumId) {
        try (EntityManager em = emf.createEntityManager();){
            em.getTransaction().begin();
            TypedQuery<Photo> query = em.createQuery("select p from Photo p where p.album.id = :albumId", Photo.class);
            query.setParameter("albumId", albumId);
            List<Photo> photos = query.getResultList();
            em.getTransaction().commit();
            return photos;
        }
    }

    static void addPhoto(EntityManagerFactory emf, Photo photo) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(photo);
            em.getTransaction().commit();
        }
    }

    static Photo getPhotoById(EntityManagerFactory emf, Integer photoId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Photo photo = em.find(Photo.class, photoId);
            em.getTransaction().commit();
            return photo;
        }
    }

    static void deletePhotoById(EntityManagerFactory emf, Integer photoId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Photo photo = em.find(Photo.class, photoId);
            em.remove(photo);
            em.getTransaction().commit();
        }
    }

    static Photo changedPhoto(EntityManagerFactory emf, Integer photoId, Photo photo) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
                Photo saved = Photos.getPhotoById(emf,photoId);
                saved.setTitle(photo.getTitle());
                saved.setUrl(photo.getUrl());
                saved.setThumbnailUrl(photo.getThumbnailUrl());
                em.merge(saved);
                em.getTransaction().commit();
                return saved;
            }
        }
    }
