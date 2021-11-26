package idv.shawnyang.config;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Configuration
	@Order(1)
	static class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**")//
					.authorizeRequests().anyRequest().authenticated()//
					.and()//
					.exceptionHandling().authenticationEntryPoint(//
							(request, response, authException) -> //
							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())//
					).and()//
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}

	@Slf4j
	@Configuration
	@Order(2)
	static class DefaultWebSecurityConfig2 extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/**")//
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
					.and()// disable login and logout page
					.oauth2Login()//
					.successHandler((request, response, authentication) -> {
						OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
						String authorizedClientRegistrationId = authToken.getAuthorizedClientRegistrationId();
						String name = authToken.getName();
						Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
						String pricipalName = authToken.getPrincipal().getName();
						// TODO auto sign up if no user
						// TODO generate JWT token and redirect the token to front end.
						response.setHeader("X-AUTH", pricipalName);
						response.getWriter().write(authorizedClientRegistrationId + ":" + pricipalName);
						response.getWriter().flush();
					}).failureHandler((request, response, exception) -> {
						// TODO redirect to front end login fail page.
					});
		}
	}

}
