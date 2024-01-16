package com.example.service;

import io.micrometer.observation.annotation.Observed;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.LiverDAO;
import com.example.entity.Auteur;
import com.example.entity.Livre;

@Service
public class LivreService implements ILivreService {

	public static final int PRODUCT_PER_PAGE = 5;
	@Autowired
	private LiverDAO livreDAO;
	
	@Transactional
	@Override
	public Page<Livre> getPaginatedLivres(Pageable pageable, String keyword){ return livreDAO.getPaginatedLivres(pageable, keyword);}
	@Transactional
	@Override
	public List<Livre> getLivres() {return livreDAO.getLivres();}

	@Transactional
	@Override
	public Livre getLivre(Long id) {return livreDAO.getLivre(id);}

	@Transactional
	@Override
	public void saveLivre(Livre livre) {livreDAO.saveLivre(livre);}

	@Transactional
	@Override
	public void deleteLivre(Long id) {livreDAO.deleteLivre(id);}

	@Transactional
	@Override
	public Livre getLivreForUsers(Long id) {return livreDAO.getLivreForUsers(id);}

	@Transactional
	@Override
	public List<Livre> getLivresForUsers() {return livreDAO.getLivresForUsers();}

	@Transactional
	@Override
	public Auteur getAuteurByLivreId(Long id) {return livreDAO.getAuteurByLivreId(id);}
	
	@Transactional
	@Override
	public void saveLivres(List<Livre> livres) {livreDAO.saveLivres(livres);}
	
	@Transactional
	@Override
	@Observed(name = "Filtrage.Livres")
	public List<Livre> search(String keyword) 
	{return livreDAO.search(keyword);}
}
