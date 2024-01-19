package com.example.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Auteur;

public interface IAuteurDAO extends JpaRepository<Auteur, String>{
	 
		
}
