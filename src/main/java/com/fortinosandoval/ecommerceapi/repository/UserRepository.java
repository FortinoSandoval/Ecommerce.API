package com.fortinosandoval.ecommerceapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fortinosandoval.ecommerceapi.models.entity.DAOUser;

@Repository
public interface UserRepository extends CrudRepository<DAOUser, Integer> {

  DAOUser findByUsername(String username);

}
