package com.example;

import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.example.entity.Adherent;
import com.example.entity.Admin;

import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class GestionBibliothequeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionBibliothequeApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

	
	@Bean
	CommandLineRunner commandLineRunnerJdbcUsers(JdbcUserDetailsManager jdbcUserDetailsManager) {
	    return args -> {
	    	if (!jdbcUserDetailsManager.userExists("user")) {
	    		com.example.entity.User adherent =  Adherent.builder()
	                    .username("user")
	                    .password(passwordEncoder().encode("user"))
	                    .enabled(true)
	                    .dtype("USER")  // Set dtype manually
	                    .build();

	            jdbcUserDetailsManager.createUser(adherent);
	            
	            
	           
	        }
	    	if (!jdbcUserDetailsManager.userExists("test")) {
	    	 //test
            com.example.entity.User adherentt =  Adherent.builder()
                    .username("test")
                    .password(passwordEncoder().encode("test"))
                    .enabled(true)
                    .dtype("USER")  // Set dtype manually
                    .build();

            jdbcUserDetailsManager.createUser(adherentt);
	    	}

	        if (!jdbcUserDetailsManager.userExists("admin")) {
	            com.example.entity.User admin = Admin.builder()
	                    .username("admin")
	                    .password(passwordEncoder().encode("admin"))
	                    .enabled(true)
	                    .dtype("ADMIN")  // Set dtype manually
	                    .build();

	            jdbcUserDetailsManager.createUser(admin);
	        }
	    };
	}

	

}
