package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.entity.Emprunt;

public interface IEmpuntService {
	Page<Emprunt> getEmpruntsPageable(Pageable pageable);
	

	Page<Emprunt> getEmpruntsWithStatusPageable(Pageable pageable, String keyword);
	
	Page<Emprunt> getEmpuntsByUsername(Pageable pageable, String username);
	
	Page<Emprunt> getHistoriqueByUsername(Pageable pageable, String username);
	
	List<Emprunt> search(String keyword, String username);
	
	List<Emprunt> search(String keyword);
	
	Emprunt getById(Long id);

	void updateAmendes (List<Emprunt> emps);
}
