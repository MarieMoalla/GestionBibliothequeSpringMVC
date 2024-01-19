package com.example.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Livre;


public interface ILivreDAO extends JpaRepository<Livre, Long>{}
