package com.ipsator.Security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	

	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestHeader= request.getHeader("Authorization");
		String username= null;
		String token=null;
		if(requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7);
			try {
				username=jwtHelper.getUsernameFromToken(token);
			}catch(IllegalArgumentException iae) {
				logger.info("Illegal Argument from username !!");
			}catch(ExpiredJwtException eje) {
				logger.info("token is expire");
			}catch(MalformedJwtException mfe) {
				logger.info("changes made in token... wrong token");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			logger.info("invalid Header value");
		}


		if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails= userDetailsService.loadUserByUsername(username);
			Boolean validateToken = jwtHelper.validateToken(token, userDetails);
			if(validateToken) {
				UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else {
				logger.info("Validations fails");
			} 
		}
		filterChain.doFilter(request, response);
	}
	
}
