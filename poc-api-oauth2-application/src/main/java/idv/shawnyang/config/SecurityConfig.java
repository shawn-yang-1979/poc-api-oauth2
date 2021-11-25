package idv.shawnyang.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated();
		http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		});
	}

}
