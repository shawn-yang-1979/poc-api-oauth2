package idv.shawnyang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;

@RestController
public class HomeController {
	@GetMapping
	public Greeting get() {
		Greeting g = new Greeting();
		g.setSayHi("Hi");
		return g;
	}

	@Getter
	@Setter
	private static class Greeting {
		private String sayHi;
	}
}
