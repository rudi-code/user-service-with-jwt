package com.rps.userservice.repo;

import com.rps.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "select * from users where username = ?1", nativeQuery = true)
    User findByUsername(String username);
}