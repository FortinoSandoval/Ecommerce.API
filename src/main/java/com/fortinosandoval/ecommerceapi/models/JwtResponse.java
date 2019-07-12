package com.fortinosandoval.ecommerceapi.models;

import java.io.Serializable;

public class JwtResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  private final String jwttoken;
  private String username;
  private String role;

  public JwtResponse(String jwttoken, String username, String role) {
    this.jwttoken = jwttoken;
    this.username = username;
    this.role = role;
  }

  public String getToken() {
    return this.jwttoken;
  }

  public String getUsername() {
    return this.username;
  }

  public String getRole() {
    return this.role;
  }
}