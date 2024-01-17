package com.example.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Authority;
import com.example.entity.Livre;
import com.example.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
@Service
public class AuthorityService implements IAuthorityService {

    @Autowired
    private EntityManager entityManager;
    
	@Override
	public Authority getAuthByUsername(String username) {
		try 
        {
			Session currentSession = entityManager.unwrap(Session.class);

	        Query<Authority> query = currentSession.createQuery("FROM Authority WHERE user.username = :username", Authority.class);
	        query.setParameter("username", username);

	        Authority auth = query.uniqueResult();
	        return auth;
        } catch (Exception ex) {System.out.println(ex.getMessage());return null;}
	}

	@Override
	public void updateAuthorityByUsername(Authority auth) {
		try
		{
			Session currentSession = entityManager.unwrap(Session.class);
			currentSession.merge(auth);
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}
	@Transactional
	public void deleteAuthorityByUsername(String username) {
		try
		{
			Session currentSession = entityManager.unwrap(Session.class);			
			Query<Authority> query = currentSession.createQuery("FROM Authority WHERE user.username = :username", Authority.class);
			query.setParameter("username", username);
			Authority auth = query.uniqueResult();
			currentSession.remove(auth);
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}

}
