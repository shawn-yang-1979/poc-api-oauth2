package idv.shawnyang.facade;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import idv.shawnyang.UserDto;
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

	public void signUp(UserDto userDto) {
		User user = new User();
		user.setUsername(userDto.getUsername());
		userRepos.save(user);
	}

	private UserDto convert(User user) {
		UserDto dto = new UserDto();
		dto.setUsername(user.getUsername());
		return dto;
	}

}
