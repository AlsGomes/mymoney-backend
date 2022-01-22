package br.com.als.mymoney.api.core.config.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.model.User;
import br.com.als.mymoney.api.domain.services.UserService;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var user = userService.findByEmail(email);
		return new AppUser(user, getAuthorities(user));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		Set<SimpleGrantedAuthority> authorities;

		authorities = user.getPermissions().stream()
				.map(p -> p.getDescription().toUpperCase())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
		return authorities;
	}
}
