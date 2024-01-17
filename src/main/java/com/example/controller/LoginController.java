package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.entity.Auteur;
import com.example.entity.Authority;
import com.example.entity.User;
import com.example.service.AuthorityService;
import com.example.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({"/","dashboard"})
@Api(value="Login Controller")
public class LoginController {

    /*@RequestMapping
    public String loginSubmit(){
        return "/pages/landing_page";
    }*/
	@Autowired
    private UserService userService;
	@Autowired
    private AuthorityService auth_service;
    @GetMapping
    public ModelAndView getHomePage(Model model,@RequestParam(defaultValue = "0") int page ) {
    	Page<User> userPage = userService.getUsers(PageRequest.of(page,5));
        User user = new User();
        user.setDtype("USER");
      	Authority auth = new Authority();
        model.addAttribute("userPage", userPage);
        model.addAttribute("u", user);model.addAttribute("auth", auth);
        model.addAttribute("currentPage", page);
        
        return new ModelAndView("pages/landing_page", model.asMap());
    }
    @GetMapping("profile")
    public Authentication authentication (Authentication auth)
    {
    	return auth;
    }


}
