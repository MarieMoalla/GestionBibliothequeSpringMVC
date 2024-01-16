package com.example.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.entity.Auteur;
import com.example.entity.Livre;

import jakarta.persistence.EntityManager;

@Repository
public class AuteurDAO implements IAuteurDAO {

    @Autowired
    private EntityManager entityManager;

    //Get Auteurs Paginated List
    public Page<Auteur> getPaginatedAuteurs(Pageable pageable)
	{
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Auteur> query = currentSession.createQuery("from Auteur", Auteur.class);
		List<Auteur> auteurs = query.getResultList();
		List<Auteur> list;

        if (auteurs.size() < startItem) {list = Collections.emptyList();} 
        else {
            int toIndex = Math.min(startItem + pageSize, auteurs.size());
            list = auteurs.subList(startItem, toIndex);
        }

        Page<Auteur> auteurPage = new PageImpl<Auteur>(list, PageRequest.of(currentPage, pageSize), auteurs.size());
        return auteurPage;
	}
    
    //Get Auteurs
    @Override
    public List<Auteur> getAuteurs() {
        try 
        {
            Session currentSession = entityManager.unwrap(Session.class);
            Query<Auteur> query = currentSession.createQuery("from Auteur", Auteur.class);
            return query.getResultList();
        } catch (Exception ex) {System.out.println(ex.getMessage());return null;}
    }

    // Get auteur by id
    @Override
    public Auteur getAuteur(Long id) {
        try 
        {
            Session currentSession = entityManager.unwrap(Session.class);
            return currentSession.get(Auteur.class, id);
        } catch (Exception ex) {System.out.println(ex.getMessage());return null;}
    }

    //Create auteur
    @Override
    public void saveAuteur(Auteur auteur) {
        try {
            Session currentSession = entityManager.unwrap(Session.class);
            currentSession.merge(auteur);
        } catch (Exception ex) {System.out.println(ex.getMessage());}
    }

    //Delete Auteur
    @Override
    public void deleteAuteur(Long id) {
        try {
            Session currentSession = entityManager.unwrap(Session.class);
            Auteur auteur = currentSession.get(Auteur.class, id);
            if (auteur != null) {
                currentSession.remove(auteur);
            }
        } catch (Exception ex) {System.out.println(ex.getMessage());}
    }

    //Sample Save Auteurs [just for adding sample data]
	@Override
	public void saveAuteurs(List<Auteur> auteurs) {
		try 
		{
			Session currentSession = entityManager.unwrap(Session.class);
	        for (Auteur auteur : auteurs) {
	        	currentSession.merge(auteur);
	        	}
		} catch (Exception ex) {System.out.println(ex.getMessage());}
	}
}
