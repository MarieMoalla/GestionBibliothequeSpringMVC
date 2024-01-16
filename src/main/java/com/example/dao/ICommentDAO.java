package com.example.dao;

import java.util.Set;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Comment;
import com.example.entity.Livre;

@Service
public interface ICommentDAO {

    void createComment(Comment comment);

    Set<Comment> getCommentsByLivre(Long livreId);
    Comment getCommentById(Long commentId);
    void deleteComment(Comment comment);
}