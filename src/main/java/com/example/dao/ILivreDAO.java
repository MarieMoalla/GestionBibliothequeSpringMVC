package com.example.dao;

import java.util.List;

import com.example.entity.Auteur;
import com.example.entity.Livre;


public interface ILivreDAO {
	List<Livre> getLivres();
	Livre getLivre(Long id);
	void saveLivre(Livre livre);
	void deleteLivre(Long id);
	
	Livre getLivreForUsers(Long id);
	List<Livre> getLivresForUsers();
	Auteur getAuteurByLivreId (Long id);
	void saveLivres(List<Livre> Livres);
	
	List<Livre> search(String keyword);
}
