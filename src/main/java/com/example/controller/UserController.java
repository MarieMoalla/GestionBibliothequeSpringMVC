 package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.dao.IAuthorityDAO;
import com.example.dao.IUserDAO;
import com.example.entity.Auteur;
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
	@Autowired
	private IAuthorityDAO auth_repo;

	@GetMapping("/{username}")
	@ApiOperation(value = "Cette operation nous permet de retourner un utilisateur demandé")
	public ModelAndView getUser( Model model, @PathVariable String username, Principal principal) 
	{
		PageRequest firstPageWithTwoElements = PageRequest.of(0, 2);
		PageRequest secondPageWithFiveElements = PageRequest.of(1, 5);
		
		Page<User> users = user_repo.findAll(firstPageWithTwoElements);

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
    
    @PostMapping("createuser")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet d'ajouter un utilisateur")
    public ModelAndView createUser(@ModelAttribute("u") User user, @ModelAttribute("auth") Authority auth ) {
        auth.setUser(user);
        if (auth.getAuthority()== "ROLE_USER")
        {
        	user_service.createAdherent(user);
        }
        else user_service.createAdmin(user);
        //user_repo.save(user);
        auth_repo.save(auth);
        return new ModelAndView("redirect:/");
    }
    
    @DeleteMapping("delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de supprimer un user précis")
    public ModelAndView deleteUser(@PathVariable String username) {
    	auth_service.deleteAuthorityByUsername(username);
        user_repo.deleteById(username);
        return new ModelAndView("redirect:/");
    }

}
