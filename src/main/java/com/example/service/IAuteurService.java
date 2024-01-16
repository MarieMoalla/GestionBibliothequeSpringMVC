package com.example.service;

import java.util.List;

import com.example.entity.Auteur;

public interface IAuteurService {

    List<Auteur> getAuteurs();

    Auteur getAuteur(Long id);

    void saveAuteur(Auteur auteur);

    void deleteAuteur(Long id);
    void saveAuteurs(List<Auteur> auteurs);
}
