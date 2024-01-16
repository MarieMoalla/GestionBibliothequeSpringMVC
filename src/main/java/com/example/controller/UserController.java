 package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.dao.IUserDAO;
import com.example.entity.Authority;
import com.example.entity.Comment;
import com.example.entity.Livre;
import com.example.entity.User;
import com.example.service.AuthorityService;
import com.example.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
@Api(value="User Controller")
public class UserController {

	@Autowired
	private IUserDAO user_repo;
	@Autowired
	private UserService user_service;
	@Autowired
	private AuthorityService auth_service;


	@GetMapping("/{username}")
	@ApiOperation(value = "Cette operation nous permet de retourner un utilisateur demandé")
	public ModelAndView getUser( Model model, @PathVariable String username, Principal principal) 
	{
		 User user = user_service.getUserByUsername(username);
		 Authority auth = auth_service.getAuthByUsername(username);
		 model.addAttribute("auth",auth);
		 model.addAttribute("user",user);
	     return new ModelAndView("UserTemplates/profile", model.asMap()); 
	}
 

    @PutMapping("updateUser/{username}")
    @ApiOperation(value = "Cette opération nous permet de modifier les données d'un livre choisi")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateUser(@PathVariable String username, @ModelAttribute("user") User user, @ModelAttribute("auth") Authority auth) {
        // Assurez-vous que l'auteur existe avant de le mettre à jour
        User existingLivre = user_service.getUserByUsername(username);
        if (existingLivre != null) {
        	existingLivre.setNom(user.getNom());
        	existingLivre.setPrenom(user.getPrenom());
        	existingLivre.setEmail(user.getEmail());
        	Authority existingAuth = auth_service.getAuthByUsername(username);
        	existingAuth.setAuthority(auth.getAuthority());
        	auth_service.updateAuthorityByUsername(existingAuth);
        	user_repo.save(existingLivre);
        }
        return new ModelAndView("redirect:/user/"+username);
    }
    

}
