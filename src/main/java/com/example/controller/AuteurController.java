package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.entity.Auteur;
import com.example.entity.Livre;
import com.example.service.AuteurService;
import com.example.service.LivreService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;

@RestController
@RequestMapping("auteurs")
@Api(value = "Auteur Controller")

public class AuteurController {

	
    @Autowired
    private AuteurService auteurService;

    @Autowired
    private LivreService livreService;
    
    
    // Get All Auteurs
    @GetMapping("")
    @ApiOperation(value = "Cette opération nous permet de recevoir la liste des auteurs")
    public ModelAndView getAuteurs(Model model,@RequestParam(defaultValue = "0") int page) 
    {
    	Page<Auteur> auteurs = auteurService.getPaginatedAuteurs(PageRequest.of(page,5));
        model.addAttribute("auteurs", auteurs);model.addAttribute("currentPage", page);
        return new ModelAndView("AuteurTemplates/listesAuteur", model.asMap());
    }
   
    // Get Auteur By ID
    @GetMapping("{id}")
    @ApiOperation(value = "Cette opération nous permet de retourner un auteur demandé")
    public ModelAndView getAuteur( Model model, @PathVariable Long id) 
    {
    	model.addAttribute("auteur", auteurService.getAuteur(id));
        return new ModelAndView("AuteurTemplates/auteurDetails", model.asMap()); 
    }
    
    
    // Get Create Auteur Form Page
    @GetMapping("createAuteurForm")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Afficher le formulaire de création d'un auteur")
    public ModelAndView getCreateAuteurForm(Model model) 
    {
        Auteur nouvelAuteur = new Auteur();
        model.addAttribute("nouvelAuteur", nouvelAuteur);
        return new ModelAndView("AuteurTemplates/ajoutAuteur", model.asMap());
    }
    
    // Create Auteur
    @PostMapping("createAuteur")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de créer un auteur")
    public ModelAndView createAuteur(@ModelAttribute("nouvelAuteur") Auteur auteur) 
    {
        auteurService.saveAuteur(auteur);
        return new ModelAndView("redirect:/auteurs");
    }

    
    // Get Update Auteur Form Page 
    @GetMapping("updateAuteurForm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Afficher le formulaire de mise à jour d'un auteur")
    public ModelAndView getUpdateAuteurForm(@PathVariable Long id, Model model) 
    {
        Auteur existingAuteur = auteurService.getAuteur(id);
        model.addAttribute("existingAuteur", existingAuteur);
        return new ModelAndView("AuteurTemplates/updateForm", model.asMap());
    }
    
    
    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de modifier les données d'un auteur choisi")
    public ModelAndView updateAuteur(@PathVariable Long id, @ModelAttribute("existingAuteur") Auteur auteur) 
    {
        Auteur existingAuteur = auteurService.getAuteur(id);
        if (existingAuteur != null) {
            auteur.setAuteur_id(id);
            auteurService.saveAuteur(auteur);
        }
        return new ModelAndView("redirect:/auteurs/{id}");
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Cette opération nous permet de supprimer un auteur précis")
    public ModelAndView deleteAuteur(@PathVariable Long id, Model model) 
    {
    	List<Livre> livresByAuteur = auteurService.getLivresByAuteur(id);

        if (livresByAuteur != null && !livresByAuteur.isEmpty()) {
            String message = "Auteur a des livres existants!";
            model.addAttribute("message", message);
            return new ModelAndView("error", model.asMap());
        } else {
            auteurService.deleteAuteur(id);
            return new ModelAndView("redirect:/auteurs");
        }
    }
    
    
    //save sample data
    @PostMapping("testsaveauteurs")
    public void testsaveauteurs(@RequestBody List<Auteur> auteurs) {auteurService.saveAuteurs(auteurs);}
}
