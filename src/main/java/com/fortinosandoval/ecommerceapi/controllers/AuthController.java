package com.fortinosandoval.ecommerceapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fortinosandoval.ecommerceapi.services.JwtUserDetailsService;

import com.fortinosandoval.ecommerceapi.config.JwtTokenUtil;
import com.fortinosandoval.ecommerceapi.models.JwtRequest;
import com.fortinosandoval.ecommerceapi.models.JwtResponse;
import com.fortinosandoval.ecommerceapi.models.UserDTO;

@RestController
@CrossOrigin
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private JwtUserDetailsService userDetailsService;

  @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(token));
  }

  @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
  public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
    if (userDetailsService.alreadyExist(user.getUsername())) {
      HttpHeaders responseHeaders = new HttpHeaders();
      return new ResponseEntity<>("Duplicated Username", responseHeaders, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(userDetailsService.save(user));
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}