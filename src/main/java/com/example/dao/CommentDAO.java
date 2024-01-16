package com.example.dao;

import com.example.entity.Auteur;
import com.example.entity.Comment;
import com.example.entity.Livre;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class CommentDAO implements ICommentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createComment(Comment comment) {
        entityManager.persist(comment);
    }
    
    @Override
    public void saveComment(Comment comment) {
        try {
            Session currentSession = entityManager.unwrap(Session.class);
            currentSession.merge(comment);
        } catch (Exception ex) {System.out.println(ex.getMessage());}
    }

    @Override
    public List<Comment> getCommentsByLivre(Long livreId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.livre.livre_id = :livre", Comment.class)
                .setParameter("livre", livreId)
                .getResultList();
    }
    @Override
    public Comment getCommentById(Long commentId) {
        return entityManager.find(Comment.class, commentId);
    }
    @Override
    public void deleteComment(Comment comment) {
        entityManager.remove(comment);
    }
    
    @Override
    public List<Comment> findSignaledComments() {
        return entityManager.createQuery(
                "SELECT c FROM Comment c WHERE  c.estSignale = true",
                Comment.class)
                .getResultList();
    }

    
}

