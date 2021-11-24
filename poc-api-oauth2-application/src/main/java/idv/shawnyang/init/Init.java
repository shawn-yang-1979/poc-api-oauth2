package idv.shawnyang.init;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import idv.shawnyang.entity.User;
import idv.shawnyang.repos.UserRepos;

@Component
@Transactional
public class Init implements CommandLineRunner {

	@Autowired
	private UserRepos userRepos;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUsername("user");
		user.setPassword(passwordEncoder.encode("user123"));
		User admin = new User();
		admin.setUsername("admin");
		admin.setPassword(passwordEncoder.encode("admin123"));
		userRepos.save(user);
		userRepos.save(admin);
	}

}
