package com.example.springdb.DB;

import com.example.springdb.entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public interface Comments {
    static List<Comment> getAllCommentsByPostId(EntityManagerFactory emf, int postId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class);
            query.setParameter("postId", postId);
            List<Comment> comments = query.getResultList();
            em.getTransaction().commit();
            return comments;
        }
    }

    static void addComment(EntityManagerFactory emf, Comment comment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
        }
    }

    static Comment findById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Comment comment = em.find(Comment.class, id);
            em.getTransaction().commit();
            return comment;
        }
    }

    static void deleteById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Comment comment = em.find(Comment.class, id);
            em.remove(comment);
            em.getTransaction().commit();
        }
    }

    static void changeComment(EntityManagerFactory emf, Comment comment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(comment);
            em.getTransaction().commit();
        }
    }
}
