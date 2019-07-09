package com.fortinosandoval.ecommerceapi.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class DAOUser {

  private long id;
  private String username;
  @JsonIgnore
  private String password;

  public DAOUser() {
  }

  public DAOUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Column(name = "username", nullable = false)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "password", nullable = false)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}