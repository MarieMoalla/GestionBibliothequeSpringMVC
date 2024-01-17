package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.entity.Livre;
import com.example.entity.User;

public interface IUserService {

	Page<User> getUsers(Pageable pageable);
	/*User getUser(int id);
	void saveUser(User user);
	void deleteUser(int id);*/
	User getUserByUsername(String username);

}
