package idv.shawnyang.config;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import idv.shawnyang.UserDto;
import idv.shawnyang.facade.UserFacade;
import lombok.Data;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Configuration
	@Order(1)
	static class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**")//
					.csrf().disable()//
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
					.and()//
					.authorizeRequests().anyRequest().authenticated()//
					.and()//
					.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)//
					.exceptionHandling().authenticationEntryPoint(//
							(request, response, authException) -> //
							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())//
					);
		}

		private static class JwtAuthenticationFilter extends OncePerRequestFilter {

			private static final String BEARER = "Bearer ";

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
				if (authHeader != null && authHeader.startsWith(BEARER)) {
					String token = StringUtils.delete(authHeader, BEARER);

					Algorithm a = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier v = JWT.require(a).build();
					DecodedJWT djwt = v.verify(token);
					String email = djwt.getSubject();

					// TODO, get roles from database and put it in authorities
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
							null, new LinkedList<>());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				filterChain.doFilter(request, response);
			}

		}

	}

	@Configuration
	@Order(2)
	static class DefaultWebSecurityConfig2 extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserFacade userFacade;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/**")//
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
					.and()// disable login and logout page
					.oauth2Login()//
					.successHandler(//
							(request, response, authentication) -> //
							{
								OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
								String authorizedClientRegistrationId = authToken.getAuthorizedClientRegistrationId();
								String pricipalName = authToken.getPrincipal().getName();
								String email = authToken.getPrincipal().getAttribute("email");
								UserDto user = this.userFacade.handleAuthenticationSuccess(email,
										authorizedClientRegistrationId, pricipalName);

								Algorithm a = Algorithm.HMAC256("secret".getBytes());
								String accessToken = JWT.create()//
										.withSubject(user.getEmail())//
										.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))//
										.withIssuer(request.getRequestURL().toString())//
										.sign(a);
								String refreshToken = JWT.create()//
										.withSubject(user.getEmail())//
										.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))//
										.withIssuer(request.getRequestURL().toString())//
										.sign(a);
								Tokens tokens = new Tokens();
								tokens.setAccessToken(accessToken);
								tokens.setRefreshToken(refreshToken);
								response.setContentType(MediaType.APPLICATION_JSON_VALUE);
								new ObjectMapper().writeValue(response.getOutputStream(), tokens);
							})
					.failureHandler(//
							(request, response, exception) -> //
							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage())//
					);
		}

		@Data
		private static class Tokens {
			private String accessToken;
			private String refreshToken;
		}

	}

}
