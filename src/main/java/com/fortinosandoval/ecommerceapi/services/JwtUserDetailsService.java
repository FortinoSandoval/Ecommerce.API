package com.fortinosandoval.ecommerceapi.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fortinosandoval.ecommerceapi.repository.UserRepository;
import com.fortinosandoval.ecommerceapi.models.entity.DAOUser;
import com.fortinosandoval.ecommerceapi.models.UserDTO;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder bcryptEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    DAOUser user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        new ArrayList<>());
  }

  public Boolean alreadyExist(String username) {
    DAOUser userExists = userRepository.findByUsername(username);
    if (userExists != null) {
      return true;
    }
    return false;
  }

  public DAOUser save(UserDTO user) throws Exception {
    DAOUser newUser = new DAOUser();
    newUser.setUsername(user.getUsername());
    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
    return userRepository.save(newUser);
  }
}