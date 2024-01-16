package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "livre")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Livre {


		  public enum Langue {
		        ARABE,
		        FRANCAIS,
		        ANGLAIS
		    }


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long livre_id;
	    private String titre;
	    private int anneePublication;
	    private String ISBN;
	    @Enumerated(EnumType.STRING)
	    private Langue langue;
	    private String description;
	    private int quantite;
	    private Double amendeParJour;
	    private String genre;

	    @ManyToOne
	    @JoinColumn(name = "auteur_id")
	    private Auteur auteur;

}
