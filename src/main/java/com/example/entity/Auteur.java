package com.example.entity;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "auteur")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Auteur {
	   public enum Status {
        DECEDE,
        VIVANT
    }
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auteur_id;

    private String nom;

    private String prenom;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    private String nationalite;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "auteur")
    private Set<Livre> livres;


}

