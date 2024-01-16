package com.example.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dao.IEmprunteDAO;
import com.example.dao.LiverDAO;
import com.example.entity.Emprunt;
import com.example.entity.Livre;
import com.example.entity.User;

import io.swagger.models.properties.DateProperty;

import com.example.entity.Emprunt.Status;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
@Service
public class EmpruntService implements IEmpuntService {

		public static final int PRODUCT_PER_PAGE = 5;
		@Autowired
		private EntityManager entityManger;
		@Autowired
		private IEmprunteDAO emprunt_repo;
		@Autowired
		private LivreService livre_service;
		
		@Override
		public Page<Emprunt> getEmpruntsPageable(Pageable pageable) {
			int pageSize = pageable.getPageSize();
	        int currentPage = pageable.getPageNumber();
	        int startItem = currentPage * pageSize;
	        List<Emprunt> Emprunts;
	        List<Emprunt> list;
	       
				Session currentSession = entityManger.unwrap(Session.class);
				Query<Emprunt> query = currentSession.createQuery("from emprunt", Emprunt.class);
				Emprunts = query.getResultList();
			
	        
	        if (Emprunts.size() < startItem) list = Collections.emptyList();
	        else {
	            int toIndex = Math.min(startItem + pageSize, Emprunts.size());
	            list = Emprunts.subList(startItem, toIndex);
	        }
	        Page<Emprunt> EmpruntPage = new PageImpl<Emprunt>(list, PageRequest.of(currentPage, pageSize), Emprunts.size());

	        return EmpruntPage;
		}

		

		@Override
		public Page<Emprunt> getEmpuntsByUsername(Pageable pageable, String username) {
		    try {
		        Session currentSession = entityManger.unwrap(Session.class);


		        Query<Emprunt> query = currentSession.createQuery(
		            "FROM Emprunt e WHERE e.adherent.username = :username",
		            Emprunt.class
		        );
		        query.setParameter("username", username);

		        int pageSize = pageable.getPageSize();
		        int currentPage = pageable.getPageNumber();
		        int startItem = currentPage * pageSize;
		        List<Emprunt> list;
		        List<Emprunt> Emprunts = query.list();
		        
		        if (Emprunts.size() < startItem) list = Collections.emptyList();
		        else {
		            int toIndex = Math.min(startItem + pageSize, Emprunts.size());
		            list = Emprunts.subList(startItem, toIndex);
		        }
		        Page<Emprunt> EmpruntPage = new PageImpl<Emprunt>(list, PageRequest.of(currentPage, pageSize), Emprunts.size());


		        return EmpruntPage;
		        
		       
		    } catch (Exception ex) {
		        System.out.println(ex.getMessage());
		        return null;
		    }
		}
		
		@Override
		public Page<Emprunt> getHistoriqueByUsername(Pageable pageable, String username) {
		    try {
		        Session currentSession = entityManger.unwrap(Session.class);
		        Emprunt.Status status = Emprunt.Status.RETOURNE;
		        Query<Emprunt> query = currentSession.createQuery(
		        	    "FROM Emprunt e WHERE e.adherent.username = :username AND e.status = :status",
		        	    Emprunt.class
		        	);
		        	query.setParameter("username", username);
		        	query.setParameter("status", Emprunt.Status.RETOURNE);



		        int pageSize = pageable.getPageSize();
		        int currentPage = pageable.getPageNumber();
		        int startItem = currentPage * pageSize;
		        List<Emprunt> list;
		        List<Emprunt> Emprunts = query.list();
		        
		        if (Emprunts.size() < startItem) list = Collections.emptyList();
		        else {
		            int toIndex = Math.min(startItem + pageSize, Emprunts.size());
		            list = Emprunts.subList(startItem, toIndex);
		        }
		        Page<Emprunt> EmpruntPage = new PageImpl<Emprunt>(list, PageRequest.of(currentPage, pageSize), Emprunts.size());

		        return EmpruntPage;
		        
		       
		    } catch (Exception ex) {
		        System.out.println(ex.getMessage());
		        return null;
		    }
		}

		@Override
		public List<Emprunt> search(String keyword, String username) {
			try {
			Session currentSession = entityManger.unwrap(Session.class);
		    if(Emprunt.Status.EMPRUNTE.toString().equals(keyword.toUpperCase()) || Emprunt.Status.EN_ATTENTE.toString().equals(keyword.toUpperCase()) || Emprunt.Status.REFUSE.toString().equals(keyword.toUpperCase()) || Emprunt.Status.RETOURNE.toString().equals(keyword.toUpperCase()) )
		    {
		    Emprunt.Status statusEnum = Emprunt.Status.valueOf(keyword.toUpperCase());
		    if(username != null)
		    {
			    Query<Emprunt> query = currentSession.createQuery(
			            "SELECT p FROM Emprunt p WHERE " +
			            "p.status = :keyword AND p.username = :username" ,
			            Emprunt.class
			    );
			    query.setParameter("keyword", statusEnum);
			    List<Emprunt> emprunt = query.getResultList();
			    return emprunt;
		    }
		    else 
		    {
		    Query<Emprunt> query = currentSession.createQuery(
		            "SELECT p FROM Emprunt p WHERE " +
		            "p.status = :keyword " ,
		            Emprunt.class
		    );
		    query.setParameter("keyword", statusEnum);
		    List<Emprunt> emprunt = query.getResultList();
		    return emprunt;
		    }
		    
			}
		    return null;
			}
			catch (Exception ex) {
		        System.out.println(ex.getMessage());
		        return null;
		    }
		}
		
		@Transactional
		public void saveEmprunt ( Emprunt emp)
		{
			try 
	        {
	            Session currentSession = entityManger.unwrap(Session.class);
	            currentSession.merge(emp);
	        } catch (Exception ex) {System.out.println(ex.getMessage());}
		}



		@Override
		public Page<Emprunt> getEmpruntsWithStatusPageable(Pageable pageable, String keyword) {
			int pageSize = pageable.getPageSize();
	        int currentPage = pageable.getPageNumber();
	        int startItem = currentPage * pageSize;
	        List<Emprunt> emps;
	        List<Emprunt> list;
	        if(keyword != null)
	        {
	        	emps = this.search(keyword);
	        }
	        else
	        {
				Session currentSession = entityManger.unwrap(Session.class);
				emps = emprunt_repo.findAll();
			}
	        
	        if (emps.size() < startItem) list = Collections.emptyList();
	        else {
	            int toIndex = Math.min(startItem + pageSize, emps.size());
	            list = emps.subList(startItem, toIndex);
	        }
	        Page<Emprunt> empPage = new PageImpl<Emprunt>(list, PageRequest.of(currentPage, pageSize), emps.size());

	        return empPage;
		}
		
		@Override
		public List<Emprunt> search(String keyword) {
		    
			Session currentSession = entityManger.unwrap(Session.class);

		    if(Emprunt.Status.RETOURNE.toString().equals(keyword.toUpperCase()) || Emprunt.Status.EN_ATTENTE.toString().equals(keyword.toUpperCase()) || Emprunt.Status.REFUSE.toString().equals(keyword.toUpperCase())|| Emprunt.Status.EMPRUNTE.toString().equals(keyword.toUpperCase()))
		    {	
		    Emprunt.Status statusEnum = Emprunt.Status.valueOf(keyword.toUpperCase());
		    Query<Emprunt> query = currentSession.createQuery(
		            "SELECT p FROM Emprunt p WHERE " +
		            "p.status = :keyword " ,
		            Emprunt.class
		    );
		    query.setParameter("keyword", statusEnum);
		    List<Emprunt> list = query.getResultList();
		    return list;
		    }
		    else return List.of();
		    
		}

		@Override
		public Emprunt getById(Long id) {
			try
			{
				Session currentSession = entityManger.unwrap(Session.class);
				Emprunt livre = currentSession.get(Emprunt.class, id);
				return livre;
			}
			catch(Exception ex){System.out.println(ex.getMessage());return null;}
		}

		@Override
		public void updateAmendes(List<Emprunt> emps) {
			for(int i = 0; i< emps.size();i++)
			{
				Date currentDate = new Date();
				if (emps.get(i).getDateRetour().compareTo(currentDate) >0 && Emprunt.Status.EMPRUNTE.toString().equals(emps.get(i).getStatus().name().toUpperCase()))
				{
				Livre livre = emps.get(i).getLivre();
				Double amende = livre.getAmendeParJour();
				Double amendes = amende * calculateDurationInDays(currentDate,emps.get(i).getDateRetour());
				
				emps.get(i).setAmendes(amendes);
				
				try {
		            Session currentSession = entityManger.unwrap(Session.class);
		            currentSession.merge(emps.get(i));
		        } catch (Exception ex) {System.out.println(ex.getMessage());}
				}
			}		
		}
		
		public int calculateDurationInDays(Date dateDebut, Date dateRetour) {
		    if (dateDebut != null && dateRetour != null) {
		        long millisecondsPerDay = 24 * 60 * 60 * 1000; // Number of milliseconds in a day
		        long durationInMillis = dateRetour.getTime() - dateDebut.getTime();
		        return (int) (durationInMillis / millisecondsPerDay);
		    } else {
		        // Handle the case where either dateDebut or dateRetour is null
		        return 0;
		    }
		}

		
}
