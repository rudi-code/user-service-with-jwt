package com.rps.userservice;

import java.util.ArrayList;

import org.aspectj.weaver.patterns.ArgsAnnotationPointcut;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rps.userservice.domain.Role;
import com.rps.userservice.domain.User;
import com.rps.userservice.service.UserService;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
//	@Bean
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveRole(new Role(null, "ROLE_USER"));
//			userService.saveRole(new Role(null, "ROLE_MANAGER"));
//			userService.saveRole(new Role(null, "ROLE_ADMIN"));
//			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
//			
//			userService.saveUser(new User(null, "saya", "saya", "123", new ArrayList<>()));
//			userService.saveUser(new User(null, "kamu", "kamu", "123", new ArrayList<>()));
//			userService.saveUser(new User(null, "dia", "dia", "123", new ArrayList<>()));
//			userService.saveUser(new User(null, "mereka", "mereka", "123", new ArrayList<>()));
//			
//			userService.addRoleToUser("saya", "ROLE_USER");
//			userService.addRoleToUser("kamu", "ROLE_MANAGER");
//			userService.addRoleToUser("dia", "ROLE_ADMIN");
//			userService.addRoleToUser("mereka", "ROLE_SUPER_ADMIN");
//			userService.addRoleToUser("saya", "ROLE_SUPER_ADMIN");
//			
//		};
//	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
