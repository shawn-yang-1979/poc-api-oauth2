package idv.shawnyang.facade;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import idv.shawnyang.UserDto;
import idv.shawnyang.entity.OAuth2User;
import idv.shawnyang.entity.User;
import idv.shawnyang.repos.UserRepos;

@Component
@Transactional
public class UserFacade {

	@Autowired
	private UserRepos userRepos;

	public Collection<UserDto> getUsers() {
		Iterable<User> users = userRepos.findAll();
		return StreamSupport.stream(users.spliterator(), false)//
				.map(this::convert)//
				.collect(Collectors.toList());
	}

	public UserDto handleAuthenticationSuccess(String email, String oauth2ProviderId, String oauth2ProviderUsername) {
		Optional<User> userOpt = userRepos.findByEmail(email);
		User user;
		OAuth2User oauth2User = new OAuth2User();
		oauth2User.setOauth2ProviderId(oauth2ProviderId);
		oauth2User.setOauth2ProviderUsername(oauth2ProviderUsername);
		if (userOpt.isPresent()) {
			user = userOpt.get();
			if (user.getOauth2Users().stream()
					.noneMatch(o -> o.getOauth2Username().equals(oauth2User.getOauth2Username()))) {
				user.addOAuth2User(oauth2User);
				user = userRepos.save(user);
			}
		} else {
			user = new User();
			user.setEmail(email);
			user.addOAuth2User(oauth2User);
			user = userRepos.save(user);
		}
		return convert(user);
	}

	private UserDto convert(User user) {
		UserDto dto = new UserDto();
		dto.setEmail(user.getEmail());
		return dto;
	}

}
