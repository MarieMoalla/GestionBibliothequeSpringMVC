package com.example.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "adherent")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder 
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("USER")
public class Adherent extends User {
	
    @Temporal(TemporalType.DATE)
    private Date abonnementDateExp= java.sql.Date.valueOf(LocalDate.now().plusYears(1));
    
    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL)
    private Set<Comment> comment;
    
    
}
