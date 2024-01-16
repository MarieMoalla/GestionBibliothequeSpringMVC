package com.example.controller;
import com.example.entity.Comment;
import com.example.entity.Livre;
import com.example.service.CommentService;
import com.example.service.LivreService;  // Assuming you have a LivreService to manage Livre entities
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("comments")
public class CommentController {

	

    @Autowired
    private CommentService commentService;

    @PostMapping("create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createComment(@RequestBody Comment comment) {
        
       
        commentService.saveComment(comment);

        return ResponseEntity.ok("Comment created successfully");
    }

    
}