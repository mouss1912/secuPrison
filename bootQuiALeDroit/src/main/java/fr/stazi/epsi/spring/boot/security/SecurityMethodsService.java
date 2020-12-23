package fr.stazi.epsi.spring.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import fr.stazi.epsi.spring.boot.entity.user.User;
import fr.stazi.epsi.spring.boot.entity.Cell;
import fr.stazi.epsi.spring.boot.repository.UserRepository;

@Service
public class SecurityMethodsService {
	@Autowired
	private UserRepository userRepository;

	public boolean canManage(Long cellId, User userId) {
		//User user = userRepository.findByUsername(principal.toString)
		User user = userRepository.findById(userId).orElse(null);		
		for (Cell cell : user.getCells()) {
			if (cell.getId().equals(cellId)) 
				return true;
		}
		return false;
		
	}

}


