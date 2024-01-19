package com.example.security;


import static org.springframework.http.HttpMethod.GET;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//Injection des dependance 
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static final String[] WHITE_LIST_URL = {
		 	
			"/auteurs/testsaveauteurs"
		 	};
 
	@Bean
	public JdbcUserDetailsManager jdbcuserDetailsmanager(DataSource dataSource)
	{
		JdbcUserDetailsManager JdbcuserDetailsmanager = new JdbcUserDetailsManager(dataSource);
		return JdbcuserDetailsmanager;
	}
	
	//@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager ()
	{
		InMemoryUserDetailsManager in = new InMemoryUserDetailsManager(
				User.withUsername("user1").password(passwordEncoder.encode("1234")).build(),
				User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build(),
				User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build()
				);
		return in;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		httpSecurity.formLogin();
		
		httpSecurity
				.authorizeHttpRequests(req ->
                req
	                .requestMatchers(WHITE_LIST_URL)
	                .permitAll()
                	.requestMatchers("/auteurs/createAuteurForm").hasRole("ADMIN")
                	.requestMatchers("/auteurs/createAuteur").hasRole("ADMIN")
                	.requestMatchers("/auteurs/updateAuteurForm/{id}").hasRole("ADMIN")
                	.requestMatchers("/auteurs/update/{id}").hasRole("ADMIN")
                	.requestMatchers("/auteurs/delete/{id}").hasRole("ADMIN")
                	.requestMatchers("/createLivreForm").hasRole("ADMIN")
                	.requestMatchers("/createLivre").hasRole("ADMIN")
                	.requestMatchers("/updateLivreForm/{id}").hasRole("ADMIN")
                	.requestMatchers("/updateLivre/{id}").hasRole("ADMIN")
                	.requestMatchers("/livres/delete/{id}").hasRole("ADMIN")
                	.requestMatchers("/emprunts/createEmpruntForm/{livreId}").hasRole("USER")
                	.requestMatchers("/emprunts/mesEmprunt/{username}").hasRole("USER")
                	.requestMatchers("/emprunts/createEmprunt").hasRole("USER")
                	.requestMatchers("/emprunts").hasRole("ADMIN")
                	.requestMatchers("/user/updateUser/{username}").hasRole("ADMIN")
                	.anyRequest()
                    .authenticated())
				.exceptionHandling().accessDeniedPage("/notAuthorized");
		
			httpSecurity
		    .sessionManagement()
		        .maximumSessions(1)
		        .maxSessionsPreventsLogin(true)
		        .and()
		    .sessionFixation().none();

		
		return httpSecurity.build();
	}
}
