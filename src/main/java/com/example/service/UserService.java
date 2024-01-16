package com.example.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Adherent;
import com.example.entity.Admin;
import com.example.entity.Auteur;
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

}
