package es.udc.fi.dc.fd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String password = (String) authentication.getCredentials();

		Optional<User> user = userRepository.findByUserName(userName);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("Username: " + userName + "does not exists");
		}

		if (!passwordEncoder.matches(password, user.get().getPassword())) {
			throw new BadCredentialsException("Incorrect password");
		}

		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority(user.get().getRole().toString()));

		return new UsernamePasswordAuthenticationToken(userName, password, grantedAuths);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}