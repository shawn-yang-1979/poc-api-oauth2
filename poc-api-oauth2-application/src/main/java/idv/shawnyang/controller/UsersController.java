package idv.shawnyang.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idv.shawnyang.UserDto;
import idv.shawnyang.facade.UserFacade;

@RestController
@RequestMapping(value = "/api/users")
public class UsersController {

	@Autowired
	private UserFacade userFacade;

	@GetMapping
	public Collection<UserDto> get() {
		return userFacade.getUsers();
	}

	@GetMapping("/current")
	public String getCurrent(Principal p) {
		return p.getName();
	}

	@PostMapping
	public void post(@RequestBody UserDto userDto) {
		userFacade.signUp(userDto);
	}
}