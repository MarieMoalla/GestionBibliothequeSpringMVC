package com.example.dao;

import com.example.entity.Comment;
import com.example.entity.Livre;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
@Transactional
public class CommentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void createComment(Comment comment) {
        entityManager.persist(comment);
    }

    public List<Comment> getCommentsByLivre(Long livreId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.livre.livre_id = :livre", Comment.class)
                .setParameter("livre", livreId)
                .getResultList();
    }
    
    public Comment getCommentById(Long commentId) {
        return entityManager.find(Comment.class, commentId);
    }

    public void deleteComment(Comment comment) {
        entityManager.remove(comment);
    }
    
}

