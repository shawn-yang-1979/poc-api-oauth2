package idv.shawnyang.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HomeController {
	@GetMapping
	public Principal get(Principal p) {
		log.info(p.getName());
		return p;
	}
}
