package com.example.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.entity.Auteur;

public interface IAuteurDAO {
	 
		List<Auteur> getAuteurs();
	    Auteur getAuteur(Long id);
	    void saveAuteur(Auteur auteur);
	    void deleteAuteur(Long id);
	    void saveAuteurs(List<Auteur> auteurs);
	    Page<Auteur> getPaginatedAuteurs(Pageable pageable);
}
