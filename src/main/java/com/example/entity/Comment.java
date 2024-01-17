package com.example.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;
    private String objet;
    private String contenu;
    private Boolean estSignale = false;
    private String raisonSign;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "livre_id")
    private Livre livre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private User adherent;
}
