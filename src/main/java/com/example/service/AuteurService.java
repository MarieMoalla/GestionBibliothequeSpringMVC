package com.example.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.IAuteurDAO;
import com.example.entity.Auteur;
import com.example.entity.Authority;
import com.example.entity.Livre;

import jakarta.persistence.EntityManager;

@Service
public class AuteurService implements IAuteurService {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private IAuteurDAO auteurDAO;

	@Transactional
	public Page<Auteur> getPaginatedLivres(Pageable pageable){ return auteurDAO.getPaginatedAuteurs(pageable);}
	
    @Override
    @Transactional
    public List<Auteur> getAuteurs() {
        return auteurDAO.getAuteurs();
    }

    @Override
    @Transactional
    public Auteur getAuteur(Long id) {
        return auteurDAO.getAuteur(id);
    }

    @Override
    @Transactional
    public void saveAuteur(Auteur auteur) {
        auteurDAO.saveAuteur(auteur);
    }

    @Override
    @Transactional
    public void deleteAuteur(Long id) {
        auteurDAO.deleteAuteur(id);
    }

	@Override
	@Transactional
	public void saveAuteurs(List<Auteur> auteurs) {
		auteurDAO.saveAuteurs(auteurs);
		
	}
	
	
	@Transactional
	public List<Livre> getLivresByAuteur(Long id) {
		try 
        {
			Session currentSession = entityManager.unwrap(Session.class);

	        Query<Livre> query = currentSession.createQuery("FROM Livre WHERE auteur.auteur_id = :auteur_id", Livre.class);
	        query.setParameter("auteur_id", id);

	        List<Livre> list = query.getResultList();
	        return list;
	        
        } catch (Exception ex) {System.out.println(ex.getMessage());return null;}
		
	}
}
