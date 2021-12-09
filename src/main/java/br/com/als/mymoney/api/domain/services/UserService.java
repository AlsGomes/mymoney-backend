package br.com.als.mymoney.api.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.model.User;
import br.com.als.mymoney.api.domain.repositories.UserRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public User findByEmail(String email) {
		var obj = repository.findByEmail(email)
				.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));

		return obj;
	}
}
