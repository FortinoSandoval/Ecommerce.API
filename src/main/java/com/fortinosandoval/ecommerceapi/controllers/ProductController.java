package com.fortinosandoval.ecommerceapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.fortinosandoval.ecommerceapi.repository.ProductRepository;
import com.fortinosandoval.ecommerceapi.services.JwtUserDetailsService;
import com.fortinosandoval.ecommerceapi.models.RequestResponse;
import com.fortinosandoval.ecommerceapi.models.entity.Product;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "product", description = "Operations partaining to Products")
public class ProductController {

  @Autowired
  private JwtUserDetailsService userDetailsService;

  @Autowired
  private ProductRepository productRepository;

  @ApiOperation(value = "View the product list", response = Iterable.class)
  @RequestMapping(value = "/products")
  public ResponseEntity<?> getAllProducts() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = userDetailsService.isAdmin(auth.getName());
    if (!isAdmin) {
      RequestResponse requestResponse = new RequestResponse("You don't have permission", "PERMISSION_DENIED",
          HttpStatus.BAD_REQUEST.value());
      return new ResponseEntity<>(requestResponse, HttpStatus.BAD_REQUEST);
    }
    List<Product> products = productRepository.findAll();
    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete a product using its id")
  @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = userDetailsService.isAdmin(auth.getName());
    if (!isAdmin) {
      RequestResponse requestResponse = new RequestResponse("You don't have permission", "PERMISSION_DENIED",
          HttpStatus.BAD_REQUEST.value());
      return new ResponseEntity<>(requestResponse, HttpStatus.BAD_REQUEST);
    }
    productRepository.deleteById(id);
    RequestResponse requestResponse = new RequestResponse("Item " + id + " was deleted", "DELETED",
          HttpStatus.OK.value());
    return new ResponseEntity<>(requestResponse, HttpStatus.OK);
  }
}
