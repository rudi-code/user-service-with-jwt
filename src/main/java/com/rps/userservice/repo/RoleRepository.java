package com.rps.userservice.repo;

import com.rps.userservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	@Query(value = "select * from role where name = ?1", nativeQuery = true)
    Role findByName(String name);
}