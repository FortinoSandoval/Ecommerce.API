package com.fortinosandoval.ecommerceapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fortinosandoval.ecommerceapi.services.JwtUserDetailsService;

import com.fortinosandoval.ecommerceapi.config.JwtTokenUtil;
import com.fortinosandoval.ecommerceapi.models.JwtRequest;
import com.fortinosandoval.ecommerceapi.models.JwtResponse;
import com.fortinosandoval.ecommerceapi.models.UserDTO;
import com.fortinosandoval.ecommerceapi.models.RequestResponse;;

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
    HttpHeaders responseHeaders = new HttpHeaders();

    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
          authenticationRequest.getPassword()));
    } catch (DisabledException e) {
      RequestResponse requestResponse = new RequestResponse("User are disabled", "DISABLED",
          HttpStatus.UNAUTHORIZED.value());
      return new ResponseEntity<>(requestResponse, responseHeaders, HttpStatus.UNAUTHORIZED);
    } catch (BadCredentialsException e) {
      RequestResponse requestResponse = new RequestResponse("Invalid username/password", "INVALID_CREDENTIALS",
          HttpStatus.UNAUTHORIZED.value());
      return new ResponseEntity<>(requestResponse, responseHeaders, HttpStatus.UNAUTHORIZED);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    final String role = userDetailsService.getUserRole(authenticationRequest.getUsername());

    return ResponseEntity.ok(new JwtResponse(token, authenticationRequest.getUsername(), role));
  }

  @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
  public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
    if (userDetailsService.alreadyExist(user.getUsername())) {
      HttpHeaders responseHeaders = new HttpHeaders();
      RequestResponse requestResponse = new RequestResponse("Duplicated username", "DUPLICATE_USERNAME",
          HttpStatus.BAD_REQUEST.value());
      return new ResponseEntity<>(requestResponse, responseHeaders, HttpStatus.BAD_REQUEST);
    }
    user.setRole("PERSON");
    return ResponseEntity.ok(userDetailsService.save(user));
  }

  @RequestMapping(value = "/isadmin", method = RequestMethod.GET)
  public boolean isAdmin() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String role = userDetailsService.getUserRole(auth.getName());

    return role.equals("ADMIN") ? true : false;
  }
}