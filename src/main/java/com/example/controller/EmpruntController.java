package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.example.dao.IEmprunteDAO;
import com.example.dao.IUserDAO;
import com.example.entity.Auteur;
import com.example.entity.Emprunt;
import com.example.entity.Livre;
import com.example.entity.User;
import com.example.service.AuteurService;
import com.example.service.EmpruntService;
import com.example.service.LivreService;
import com.example.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/emprunts")
@Api(value = "Emprunt Controller")
public class EmpruntController {

	@Autowired
    private EmpruntService empruntService;	
	@Autowired
	private IEmprunteDAO emp_repo;
	@Autowired
	private LivreService livre_repo;
	@Autowired
	private UserService user_repo;
		
	@GetMapping(value = "/all")
	@PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de recevoir la liste des emprunts")
    public ModelAndView getAllEmprunts(Model model,@RequestParam(defaultValue = "0") int page,@Param("keyword") String keyword ) 
	{
		//update amende if exist
		Page<Emprunt> empruntPage = empruntService.getEmpruntsWithStatusPageable(PageRequest.of(page,5),keyword);
		
		empruntService.updateAmendes(empruntPage.getContent());
        model.addAttribute("emprunts", empruntPage);
        model.addAttribute("currentPage", page);
       
        return new ModelAndView("EmpruntTemplates/ListesEmprunt", model.asMap());
    }
	
	@GetMapping(value = "/mesEmprunt/{username}")
	@PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Cette opération nous permet de recevoir la liste des emprunts")
    //public ModelAndView getEmprunts(Model model,@RequestParam(defaultValue = "0") int page,@Param("keyword") String keyword ) 
    public ModelAndView getEmprunts(@PathVariable String username,@RequestParam(defaultValue = "0") int page, Model model ) 
	{
		//update amende if exist
		Page<Emprunt> emps = empruntService.getEmpuntsByUsername(PageRequest.of(page,5), username);
		empruntService.updateAmendes(emps.getContent());
        model.addAttribute("emprunts", emps);
        model.addAttribute("username", username);
        model.addAttribute("currentPage", page);
       
        return new ModelAndView("EmpruntTemplates/mesEmpruntes", model.asMap());
    }

	@GetMapping(value = "/mesEmprunt/historique/{username}")
	@PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Cette opération nous permet de recevoir la liste des emprunts")
    public ModelAndView getEmpruntsHistorique(@PathVariable String username,@RequestParam(defaultValue = "0") int page, Model model ) 
	{
		Page<Emprunt> emps = empruntService.getHistoriqueByUsername(PageRequest.of(page,5), username);
		
        model.addAttribute("emprunts", emps);
        model.addAttribute("username", username);
        model.addAttribute("currentPage", page);
       
        return new ModelAndView("EmpruntTemplates/historiquesEmprunt", model.asMap());
    }
	
	/* Form create   */ 
    @GetMapping("/createEmpruntForm/{livreId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Afficher le formulaire d'ajout d'une emprunte")
    public ModelAndView getCreateEmpruntForm(@PathVariable Long livreId,Model model) {
    	Emprunt nouvelEmp = new Emprunt();
    	Livre livre = livre_repo.getLivre(livreId); 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    	String username = userDetails.getUsername();
    	User user = user_repo.getUserByUsername(username);
    	nouvelEmp.setAdherent(user);
    	nouvelEmp.setLivre(livre);
        model.addAttribute("nouvelEmp", nouvelEmp);
        model.addAttribute("user", user);
        model.addAttribute("livre", livre);
        return new ModelAndView("EmpruntTemplates/ajoutEmprunt", model.asMap());
    }
    
    @PostMapping("/createEmprunt/{livreId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Cette opération nous permet d'ajouter un Emprunt")
    public ModelAndView createEmprunt(@ModelAttribute("nouvelEmp") Emprunt nouvelEmp, @PathVariable Long livreId,Model model) {
    	Livre livre = livre_repo.getLivre(livreId); 
        if (nouvelEmp.getNbCopies() > 1 && nouvelEmp.getNbCopies() <= livre.getQuantite()) {
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    	String username = userDetails.getUsername();
	    	User user = user_repo.getUserByUsername(username);
	    	nouvelEmp.setLivre(livre);	
	    	nouvelEmp.setAdherent(user);
	    	
	    	//changrer nombre des copie dun livre :
	    	livre.setQuantite(livre.getQuantite()-nouvelEmp.getNbCopies());
	    	livre_repo.saveLivre(livre);
	    	empruntService.saveEmprunt(nouvelEmp);
	        return new ModelAndView("redirect:/emprunts/mesEmprunt/"+ username);
        } 
        else {
        	String msg = "Nombre copie choisi ("+nouvelEmp.getNbCopies()+") doit etre entre : 1 et " + livre.getQuantite()+"." ;
        	model.addAttribute("message",msg);
        	return new ModelAndView("/error", model.asMap());
        }
    }
    

    //form Upadate
  	@GetMapping("/update/{id}")
  	@PreAuthorize("hasRole('ADMIN')")
      @ApiOperation(value = "Afficher le formulaire de mise à jour d'un livre")
      public ModelAndView getUpdateEmpruntForm(@PathVariable Long id, Model model) {
          // Retrieve the existing Auteur object from the database based on the ID
          Emprunt emp = empruntService.getById(id);
          Date dateDebut = emp.getDateRetour();
          Date dateRetour = emp.getDateRetour();
          // Add the existing Auteur object to the model
          model.addAttribute("emp", emp);
          model.addAttribute("dateDebut", dateDebut);
          model.addAttribute("dateRetour", dateRetour);
          // Return the Thymeleaf view name (updateAuteurForm.html)
          return new ModelAndView("EmpruntTemplates/updateEmprunt", model.asMap());
      }
      
      
      @PutMapping("/updateEmprunt/{id}")
      @PreAuthorize("hasRole('ADMIN')")
      @ApiOperation(value = "Cette opération nous permet de modifier les données d'un livre choisi")
      public ModelAndView updateEmprunt(@PathVariable Long id, @ModelAttribute("emp") Emprunt emp, Model model) {
          // Assurez-vous que l'auteur existe avant de le mettre à jour
          Emprunt existingEmp = empruntService.getById(id);
          
          if (existingEmp != null) {
            if( existingEmp.getStatus() != emp.getStatus() ) 
            {
            	if(((Emprunt.Status.RETOURNE.toString().equals(emp.getStatus().name()) && !Emprunt.Status.REFUSE.toString().equals(existingEmp.getStatus().name())) || (Emprunt.Status.REFUSE.toString().equals(emp.getStatus().name()) && !Emprunt.Status.RETOURNE.toString().equals(existingEmp.getStatus().name()))))
            	{
            		Livre livre = livre_repo.getLivre(existingEmp.getLivre().getLivre_id());
            		livre.setQuantite(livre.getQuantite()+existingEmp.getNbCopies());
            		livre_repo.saveLivre(livre);
            	}
            	if(((Emprunt.Status.EN_ATTENTE.toString().equals(emp.getStatus().name()) && !Emprunt.Status.EMPRUNTE.toString().equals(existingEmp.getStatus().name())) || (Emprunt.Status.EMPRUNTE.toString().equals(emp.getStatus().name()) && !Emprunt.Status.EN_ATTENTE.toString().equals(existingEmp.getStatus().name()))))
            	{
            		Livre livre = livre_repo.getLivre(existingEmp.getLivre().getLivre_id());
            		livre.setQuantite(livre.getQuantite()-existingEmp.getNbCopies());
            		livre_repo.saveLivre(livre);
            	}
            }
          	existingEmp.setDateDebut(emp.getDateDebut());
          	existingEmp.setDateRetour(emp.getDateRetour());
          	existingEmp.setStatus(emp.getStatus());
          	
          	if(!existingEmp.getDateDebut().before(existingEmp.getDateRetour()))
          	{
          	  String msg = "Date debut est après la date du retour" ;
          	  model.addAttribute("message",msg);
        	  return new ModelAndView("/error", model.asMap());
        	}
          	if(emp.getDateDebut()==null || emp.getDateRetour()==null)
          	{
          		String msg = "Date retour ou date debut est de valeur null" ;
          		model.addAttribute("message",msg);
          		return new ModelAndView("/error", model.asMap());
          	}
          	empruntService.saveEmprunt(existingEmp);
          	return new ModelAndView("redirect:/emprunts/all");
          }
          else {
        	  String msg = "Utilisateur introuvable" ;
          	  model.addAttribute("message",msg);
        	  return new ModelAndView("/error", model.asMap());}
          
      }
    
	@GetMapping(value = "/{username}")
    @ApiOperation(value = "Cette opération nous permet de recevoir la liste des emprunts par utilisateur")
    public ModelAndView getEmpruntsByUserId(Model model,@RequestParam(defaultValue = "0") int page,@Param("keyword") String keyword, @PathVariable String username ) 
	{
		Page<Emprunt> empruntPage = empruntService.getEmpuntsByUsername(PageRequest.of(page,5), username);
        model.addAttribute("emprunts", empruntPage);
        model.addAttribute("currentPage", page);
       
        return new ModelAndView("EmpruntTemplates/ListesEmprunt", model.asMap());
    }
	
    
}
