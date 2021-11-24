package idv.shawnyang.service;

import java.util.LinkedList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import idv.shawnyang.entity.User;
import idv.shawnyang.repos.UserRepos;

@Component
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepos userRepos;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userOp = userRepos.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return new org.springframework.security.core.userdetails.User(userOp.getUsername(), userOp.getPassword(),
				new LinkedList<>());
	}

}
