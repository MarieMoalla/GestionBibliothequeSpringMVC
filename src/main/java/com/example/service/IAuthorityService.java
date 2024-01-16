package com.example.service;

import com.example.entity.Authority;

public interface IAuthorityService {

	Authority getAuthByUsername (String username);
	void updateAuthorityByUsername(Authority auth);
}
