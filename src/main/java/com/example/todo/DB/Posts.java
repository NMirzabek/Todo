package com.example.springdb.DB;

import com.example.springdb.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public interface Posts {
    static List<Post> getPostsByUserId(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<Post> query = em.createQuery("select p from Post p where p.user.id = :id", Post.class);
            query.setParameter("id", id);
            List<Post> posts = query.getResultList();
            em.getTransaction().commit();
            return posts;
        }
    }

    static void addPost(EntityManagerFactory emf, Post post) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(post);
            em.getTransaction().commit();
        }
    }


    static void deletePostById(EntityManagerFactory emf, int postId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete  from  Comment c where c.post.id = :postId").setParameter("postId", postId).executeUpdate();
            Post post = em.find(Post.class, postId);
            em.remove(post);
            em.getTransaction().commit();
        }
    }

    static Post getPostById(EntityManagerFactory emf, int postId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Post post = em.find(Post.class, postId);
            em.getTransaction().commit();
            return post;
        }
    }

    static Post changePost(EntityManagerFactory emf, Post post) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Post changingPost = em.find(Post.class, post.getId());
            changingPost.setBody(post.getBody());
            changingPost.setTitle(post.getTitle());
            em.merge(changingPost);
            em.getTransaction().commit();
            return changingPost;
        }
    }
}
