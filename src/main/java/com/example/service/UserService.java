package com.example.service;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Adherent;
import com.example.entity.Admin;
import com.example.entity.Auteur;
import com.example.entity.Livre;
import com.example.entity.User;

import jakarta.persistence.EntityManager;

@Service
public class UserService implements IUserService{

    @Autowired
    private EntityManager entityManager;
    
	/*//@Transactional délégue la gestion des transactions au conteneur IOC
	@Transactional
	@Override
	public List<User> getUsers() {return userDAO.getUsers();}

	@Transactional
	@Override
	public User getUser(int id) {return userDAO.getUser(id);}

	@Transactional
	@Override
	public void saveUser(User user) {userDAO.saveUser(user);}

	@Transactional
	@Override
	public void deleteUser(int id) {userDAO.deleteUser(id);}

*/
	@Transactional
	@Override
	public User getUserByUsername(String username) {
		try 
        {
            Session currentSession = entityManager.unwrap(Session.class);
            return currentSession.get(User.class, username);
        } catch (Exception ex) {System.out.println(ex.getMessage());return null;}
    }

	@Override
	public Page<User> getUsers(Pageable pageable) {
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> users;
        List<User> list;
        
        
			Session currentSession = entityManager.unwrap(Session.class);
			Query<User> query = currentSession.createQuery("from User", User.class);
			users = query.getResultList();
		
        
        if (users.size() < startItem) list = Collections.emptyList();
        else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }
        Page<User> userPage = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), users.size());

        return userPage;
	}

}
