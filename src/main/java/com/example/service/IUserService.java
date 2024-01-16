package com.example.service;

import java.util.List;

import com.example.entity.User;

public interface IUserService {

	/*List<User> getUsers(String role);
	User getUser(int id);
	void saveUser(User user);
	void deleteUser(int id);*/
	User getUserByUsername(String username);

}
