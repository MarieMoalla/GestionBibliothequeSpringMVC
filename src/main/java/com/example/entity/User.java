package com.example.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.InheritanceType;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Data
@SuperBuilder 
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{

    @Id
    @Column(name = "username", length = 50)
    private String username;
    private String nom;
    private String prenom;
    private String email;
    @Column(name = "password", length = 500, nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "dtype", nullable = false, insertable=false, updatable=false,columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private String dtype;


   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
       String authority = "ROLE_" + this.getDtype();
       return Collections.singletonList(new SimpleGrantedAuthority(authority));
   }


@Override
public boolean isAccountNonExpired() {
	// TODO Auto-generated method stub
	return true;
}

@Override
public boolean isAccountNonLocked() {
	// TODO true-generated method stub
	return false;
}

@Override
public boolean isCredentialsNonExpired() {
	// TODO Auto-generated method stub
	return true;
}
}

