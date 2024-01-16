package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.example.dao.CommentDAO;
import com.example.entity.Auteur;
import com.example.entity.Comment;
import com.example.entity.Livre;
import com.example.entity.User;
import com.example.service.AuteurService;
import com.example.service.CommentService;
import com.example.service.LivreService;
import com.example.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("")
@Api(value = "Livre Controller")
public class LivreController {

	@Autowired
    private LivreService livreService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private AuteurService auteurService;	
	
	 @Autowired
	    private CommentService commentService;
	 @Autowired
	    private CommentDAO commentDAO;
	
	@GetMapping(value = "livres")
    @ApiOperation(value = "Cette opération nous permet de recevoir la liste des livres")
    public ModelAndView getLivres(Model model,@RequestParam(defaultValue = "0") int page,@Param("keyword") String keyword ) 
	{

        Page<Livre> livrePage = livreService.getPaginatedLivres(PageRequest.of(page,5), keyword);
        model.addAttribute("livres", livrePage);
        model.addAttribute("currentPage", page);
       
        return new ModelAndView("LivreTemplates/ListesLivre", model.asMap());
    }
	
	/* Form create   */ 
    @GetMapping("createLivreForm")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Afficher le formulaire d'ajout d'un livre")
    public ModelAndView getCreateLivreForm(Model model) {
        Livre nouvelLivre = new Livre();
        List<Auteur> auteurs = new ArrayList<Auteur>();
        Auteur auteur = new Auteur();
        auteurs = auteurService.getAuteurs();
        model.addAttribute("nouvelLivre", nouvelLivre);
        model.addAttribute("auteurs", auteurs);
        model.addAttribute("auteur", auteur);
        return new ModelAndView("LivreTemplates/ajoutLivre", model.asMap());
    }


    @PostMapping("createLivre")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet d'ajouter un livre")
    public ModelAndView createLivre(@ModelAttribute("nouvelLivre") Livre livre, @ModelAttribute("auteur") Auteur auteur ) {
        Auteur selectedAuteur = auteurService.getAuteur(auteur.getAuteur_id());
        livre.setAuteur(selectedAuteur);
        livreService.saveLivre(livre);
        return new ModelAndView("redirect:/livres/" + livre.getLivre_id());
    }

	
	@GetMapping("updateLivreForm/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Afficher le formulaire de mise à jour d'un livre")
    public ModelAndView getUpdateLivreForm(@PathVariable Long id, Model model) {
        // Retrieve the existing Auteur object from the database based on the ID
        Livre livre = livreService.getLivre(id);

        // Add the existing Auteur object to the model
        model.addAttribute("livre", livre);

        // Return the Thymeleaf view name (updateAuteurForm.html)
        return new ModelAndView("LivreTemplates/updateLivre", model.asMap());
    }
    
    
    @PutMapping("updateLivre/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de modifier les données d'un livre choisi")
    public ModelAndView updateLivre(@PathVariable Long id, @ModelAttribute("existingLivre") Livre livre) {
        // Assurez-vous que l'auteur existe avant de le mettre à jour
        Livre existingLivre = livreService.getLivre(id);
        if (existingLivre != null) {
        	livre.setLivre_id(id);
        	livre.setAuteur(existingLivre.getAuteur());
            livreService.saveLivre(livre);
        }
        return new ModelAndView("redirect:/livres/{id}");
    }
    
    @GetMapping("livres/{id}")
    @ApiOperation(value = "Cette opération nous permet de retourner un livre demandé")
    public ModelAndView getLivre( Model model, @PathVariable Long id, Principal principal) {
    	 List<Comment> comments = commentService.getCommentsByLivre(id);
    	model.addAttribute("comments", comments);
    	model.addAttribute("livre", livreService.getLivre(id));
    	 model.addAttribute("principal", principal);

    	model.addAttribute("auteur",auteurService.getAuteur(livreService.getLivre(id).getAuteur().getAuteur_id()));
        return new ModelAndView("LivreTemplates/livreDetails", model.asMap()); 
    }
    
    @DeleteMapping("livres/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de supprimer un livre précis")
    public ModelAndView deleteLivre(@PathVariable Long id) {
        livreService.deleteLivre(id);
        return new ModelAndView("redirect:/livres");
    }
	
    //save sample data
    @PostMapping("testsavelivres")
    public void testsavelivres(@RequestBody List<Livre> livres) {
        livreService.saveLivres(livres);
    }
    

    //add comment
    @PostMapping("livres/{livreId}/addComment")
    public ModelAndView addComment(@PathVariable Long livreId, @RequestParam String commentContent) {
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	    
    	    // Assuming your User entity has a username property
    	    String username = authentication.getName();
    	    
    	    // Retrieve the user by username (you might need to implement this method in your user service)
    	    User user = userService.getUserByUsername(username);
    	Livre livre = livreService.getLivre(livreId);

        Comment comment = new Comment();
        comment.setContenu(commentContent);
        comment.setLivre(livre);
        comment.setAdherent(user);

        commentService.saveComment(comment);

        // Redirect back to the Livre detail page
        return new ModelAndView("redirect:/livres/"+ livreId); 
    }
    
    
    //delete comment
    @DeleteMapping("livres/comments/{commentId}")
    public ModelAndView deleteComment(@PathVariable Long commentId, Principal principal) {
        // Retrieve authenticated user
        String username = principal.getName();
        User authenticatedUser = userService.getUserByUsername(username);
        Comment commentToDelete = commentDAO.getCommentById(commentId);
        Long livreId = commentToDelete.getLivre().getLivre_id();
        // Delete comment if it belongs to the authenticated user
        commentService.deleteComment(commentId, authenticatedUser);
        

        // Redirect back to the Livre detail page
        return new ModelAndView("redirect:/livres/"+livreId); 
    }
}

