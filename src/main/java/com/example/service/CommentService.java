package com.example.service;

import com.example.dao.CommentDAO;
import com.example.entity.Comment;
import com.example.entity.Livre;
import com.example.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Override
    public void saveComment(Comment comment) {
        commentDAO.createComment(comment);
    }
    @Override
    public List<Comment> getCommentsByLivre(Long livreId) {
        return commentDAO.getCommentsByLivre(livreId);
    }
    @Override
    public void deleteComment(Long commentId, User authenticatedUser) {
        Comment comment = commentDAO.getCommentById(commentId);

        if (comment != null ) {
            commentDAO.deleteComment(comment);
        } else {
            // Throw an exception or handle unauthorized deletion
            throw new IllegalArgumentException("Comment not found or unauthorized to delete.");
        }
    }
    
    @Override
    public List<Comment> getCommentairesSignales() {
        return commentDAO.findSignaledComments();
    }
    
    @Override
    @Transactional
    public void resetSignalement(Long commentId) {
        Comment comment = commentDAO.getCommentById(commentId);
        if (comment != null) {
            comment.setEstSignale(false);
            commentDAO.saveComment(comment);
        }
    }
    
	/*
    @Transactional
	@Override
	public void saveComment(Comment comment) {commentDAO.saveComment(comment);}
*/
}