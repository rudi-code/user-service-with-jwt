package com.rps.userservice.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rps.userservice.domain.Role;
import com.rps.userservice.domain.User;
import com.rps.userservice.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserResource {

	@Autowired
    private UserService userService;


    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }
    
    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
    	URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
    			.path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
    
    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
    	URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
    			.path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
    
    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addROleToUser(@RequestBody RoleToUserForm form){
    	userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
    private static final String AUTHORIZATION = "Authorization";
    
    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
    	String authirizationHeader = request.getHeader(AUTHORIZATION);
    	if(authirizationHeader != null && authirizationHeader.startsWith("Bearer ")) {
			try {
				String refesh_token = authirizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refesh_token);
				String username = decodedJWT.getSubject();
				User user = userService.getUser(username);
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream()
								.map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> tokens =  new HashMap<String, String>();
				tokens.put("access_token", access_token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

			} catch (Exception e) {
				// TODO: handle exception
//				log.error("Error logging in: {}", e.getMessage());
				response.setHeader("error", e.getMessage());
//				response.setStatus(402);
				Map<String, String> error =  new HashMap<String, String>();
				error.put("error_message", e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh Token is missing");
		}
    }
    
    @Data
    class RoleToUserForm {
    	private String username;
    	private String roleName;
    }
    
}
