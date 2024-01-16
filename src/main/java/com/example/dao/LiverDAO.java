package com.example.dao;

import java.util.ArrayList;
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
import com.example.entity.User;

import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.EntityManager;

@Repository
public class LiverDAO implements ILivreDAO {

	//When workign with spring MVC and Hibernate, in DAO implementation we inject the session factory to get the current session.
	//In JPA we use entity manager to get current session
	//Autowired : allows Spring to resolve and inject collaborating beans into our bean
	@Autowired
	private EntityManager entityManger;
	
	
	public Page<Livre> getPaginatedLivres(Pageable pageable, String keyword)
	{
		
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Livre> livres;
        List<Livre> list;
        if(keyword != null)
        {
        	livres = this.search(keyword);
        }
        else
        {
			Session currentSession = entityManger.unwrap(Session.class);
			Query<Livre> query = currentSession.createQuery("from Livre", Livre.class);
			livres = query.getResultList();
		}
        
        if (livres.size() < startItem) list = Collections.emptyList();
        else {
            int toIndex = Math.min(startItem + pageSize, livres.size());
            list = livres.subList(startItem, toIndex);
        }
        Page<Livre> livrePage = new PageImpl<Livre>(list, PageRequest.of(currentPage, pageSize), livres.size());

        return livrePage;
        
	}
	
	@Override
	public List<Livre> getLivres() {
		try
		{
			Session currentSession = entityManger.unwrap(Session.class);
			Query<Livre> query = currentSession.createQuery("from Livre", Livre.class);
			List<Livre> list = query.getResultList();
			return list;
		}
		catch(Exception ex) {System.out.println(ex.getMessage());return null;}
		}

	@Override
	public Livre getLivre(Long id) {
		try
		{
			Session currentSession = entityManger.unwrap(Session.class);
			Livre livre = currentSession.get(Livre.class, id);
			return livre;
		}
		catch(Exception ex){System.out.println(ex.getMessage());return null;}
	}

	@Override
	public void saveLivre(Livre livre) {
		try
		{
			Session currentSession = entityManger.unwrap(Session.class);
			currentSession.merge(livre);
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}

	@Override
	public void deleteLivre(Long id) {
		try
		{
			Session currentSession = entityManger.unwrap(Session.class);
			Livre livre = currentSession.get(Livre.class, id);
			currentSession.remove(livre);
		}
		catch(Exception ex){System.out.println(ex.getMessage());}
	}

	@Override
	public Livre getLivreForUsers(Long id) {
		 try {
		        Session currentSession = entityManger.unwrap(Session.class);

		        Query<Livre> query = currentSession.createQuery("FROM Livre WHERE id = :id AND quantite > 0", Livre.class);
		        query.setParameter("id", id);

		        Livre livre = query.uniqueResult();
		        return livre;
		    } catch (Exception ex) {System.out.println(ex.getMessage());return null;}
	}

	@Override
	public List<Livre> getLivresForUsers() {
		try {
	        Session currentSession = entityManger.unwrap(Session.class);
	        Query<Livre> query = currentSession.createQuery("FROM Livre WHERE quantite > 0", Livre.class);

	        List<Livre> list = query.getResultList();
	        return list;
	    } catch (Exception ex) {
	        System.out.println(ex.getMessage());
	        return null;
	    }
	}

	@Override
	public Auteur getAuteurByLivreId(Long id) {
		try {
	        Session currentSession = entityManger.unwrap(Session.class);

	        Query<Auteur> query = currentSession.createQuery("SELECT l.auteur FROM Livre l WHERE l.livre_id = :livreId", Auteur.class);
	        query.setParameter("livreId", id);

	        return query.uniqueResult();
	    } catch (Exception ex) {System.out.println(ex.getMessage());return null;
	    }
	}
	@Override
	public void saveLivres(List<Livre> Livres) {
		try 
		{
			Session currentSession = entityManger.unwrap(Session.class);
	        for (Livre livre : Livres) {
	        	currentSession.merge(livre);
	        	}
    } 	catch (Exception ex) {System.out.println(ex.getMessage());}
	}
	
	@Override
	public List<Livre> search(String keyword) {
	    
		Session currentSession = entityManger.unwrap(Session.class);

	    if(Livre.Langue.ARABE.toString().equals(keyword.toUpperCase()) || Livre.Langue.FRANCAIS.toString().equals(keyword.toUpperCase()) || Livre.Langue.ANGLAIS.toString().equals(keyword.toUpperCase()))
	    {	
	    Livre.Langue langueEnum = Livre.Langue.valueOf(keyword.toUpperCase());
	    Query<Livre> query = currentSession.createQuery(
	            "SELECT p FROM Livre p WHERE " +
	            "p.langue = :keyword " ,
	            Livre.class
	    );
	    query.setParameter("keyword", langueEnum);
	    List<Livre> list = query.getResultList();
	    return list;
	    }
	    else 
	    {
	    Query<Livre> query = currentSession.createQuery(
	            "SELECT p FROM Livre p WHERE " +
	            "p.titre LIKE CONCAT('%', :keyword, '%') OR " +
	            "p.genre LIKE CONCAT('%', :keyword, '%') OR " +
	            "CONCAT(p.ISBN, '') LIKE CONCAT('%', :keyword, '%')",
	            Livre.class
	    );

	    query.setParameter("keyword", keyword);
	    List<Livre> list = query.getResultList();
	    return list;
		}
	    
	}



}
