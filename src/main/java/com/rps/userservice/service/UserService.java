package com.rps.userservice.service;

import com.rps.userservice.domain.Role;
import com.rps.userservice.domain.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String role);
    User getUser(String username);
    List<User> getUsers();

}
