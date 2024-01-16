package com.example.service;

import com.example.dao.CommentDAO;
import com.example.entity.Comment;
import com.example.entity.Livre;
import com.example.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    public void saveComment(Comment comment) {
        commentDAO.createComment(comment);
    }

    public List<Comment> getCommentsByLivre(Long livreId) {
        return commentDAO.getCommentsByLivre(livreId);
    }
    
    public void deleteComment(Long commentId, User authenticatedUser) {
        Comment comment = commentDAO.getCommentById(commentId);

        if (comment != null && comment.getAdherent().equals(authenticatedUser)) {
            commentDAO.deleteComment(comment);
        } else {
            // Throw an exception or handle unauthorized deletion
            throw new IllegalArgumentException("Comment not found or unauthorized to delete.");
        }
    }
}