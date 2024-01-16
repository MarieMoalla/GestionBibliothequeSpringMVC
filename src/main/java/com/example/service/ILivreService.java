package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.entity.Auteur;
import com.example.entity.Livre;

public interface ILivreService {

	List<Livre> getLivres();
	Livre getLivre(Long id);
	void saveLivre(Livre livre);
	void deleteLivre(Long id);
	
	Livre getLivreForUsers(Long id);
	List<Livre> getLivresForUsers();
	Auteur getAuteurByLivreId (Long id);
	void saveLivres(List<Livre> livres);
	
	Page<Livre> getPaginatedLivres(Pageable pageable, String keyword);
	
	List<Livre> search(String keyword);
	
}
