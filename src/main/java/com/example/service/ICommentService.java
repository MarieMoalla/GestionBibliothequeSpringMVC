package com.example.service;

import java.util.List;

import com.example.entity.Comment;
import com.example.entity.User;


public interface ICommentService {
	void saveComment(Comment comment);

	void deleteComment(Long commentId, User authenticatedUser);

	List<Comment> getCommentsByLivre(Long livreId);

	List<Comment> getCommentairesSignales();

	void resetSignalement(Long commentId);
	
}
