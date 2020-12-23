package fr.stazi.epsi.spring.boot.security;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.stazi.epsi.spring.boot.entity.user.Right;
import fr.stazi.epsi.spring.boot.entity.user.Role;
import fr.stazi.epsi.spring.boot.entity.user.User;
import fr.stazi.epsi.spring.boot.repository.UserRepository;

@Service
@Transactional
public class SecurityUserService implements UserDetailsService {

	private UserRepository userRepository;
	
	public SecurityUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getLabel()));			
				for (Right right : role.getRights()) {
					authorities.add(new SimpleGrantedAuthority(right.getLabel()));
				}
			}
		}
					
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

}
