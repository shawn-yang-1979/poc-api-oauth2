package idv.shawnyang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping("/login-success")
	public String loginSuccess(@RequestParam String token) {
		return token;
	}

	@GetMapping("/login-failure")
	public String loginFailure() {
		return "failure";
	}
}