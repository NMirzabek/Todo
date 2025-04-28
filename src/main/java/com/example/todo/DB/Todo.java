package com.example.springdb.DB;

import com.example.springdb.entity.ToDos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public interface Todo {
    static List<ToDos> getTodosByUserId(EntityManagerFactory emf, int user_id) {
        try (EntityManager em = emf.createEntityManager();){
            em.getTransaction().begin();
            TypedQuery<ToDos> query = em.createQuery("select t from ToDos t where t.user.id=:user_id", ToDos.class);
            query.setParameter("user_id", user_id);
            List<ToDos> resultList = query.getResultList();
            em.getTransaction().commit();
            return resultList;
        }
    }

    static void addTodo(EntityManagerFactory emf, ToDos toDos) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(toDos);
            em.getTransaction().commit();
        }
    }

    static ToDos deleteById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            ToDos toDos = em.find(ToDos.class, id);
            em.remove(toDos);
            em.getTransaction().commit();
            return toDos;
        }
    }

    static ToDos getById(EntityManagerFactory emf, int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            ToDos toDos = em.find(ToDos.class, id);
            em.getTransaction().commit();
            return toDos;
        }
    }

    static void update(EntityManagerFactory emf, ToDos existingToDo) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(existingToDo);
            em.getTransaction().commit();
        }
    }
}
