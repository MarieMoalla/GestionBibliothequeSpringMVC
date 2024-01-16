package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Emprunt;
import com.example.entity.User;


public interface IUserDAO extends JpaRepository<User,String>{
}
