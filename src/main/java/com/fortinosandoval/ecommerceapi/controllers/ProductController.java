package com.fortinosandoval.ecommerceapi.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    if (!isAdmin()) {
      return noPermission();
    }
    List<Product> products = productRepository.findAll();
    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @ApiOperation(value = "Search for a product using its id", response = Iterable.class)
  @RequestMapping(value = "/products/{id}")
  public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
    if (!isAdmin()) {
      return noPermission();
    }
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (!optionalProduct.isPresent()) {
      return badRequest("NOT_FOUND", "Product not found", 400, HttpStatus.BAD_REQUEST);
    }
    Product product = optionalProduct.get();
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete a product using its id")
  @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id) {
    if (!isAdmin()) {
      return noPermission();
    }
    productRepository.deleteById(id);
    RequestResponse requestResponse = new RequestResponse("Item " + id + " was deleted", "DELETED",
        HttpStatus.OK.value());
    return new ResponseEntity<>(requestResponse, HttpStatus.OK);
  }

  @ApiOperation(value = "Create a new record for products")
  @RequestMapping(value = "/products", method = RequestMethod.POST)
  public ResponseEntity<?> addProduct(@RequestBody Product newProduct) {

    if (!isAdmin()) {
      return noPermission();
    }

    Product product = productRepository.save(newProduct);

    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @ApiOperation(value = "Update an existing record")
  @RequestMapping(value = "/products", method = RequestMethod.PUT)
  public ResponseEntity<?> updateProduct(@RequestBody Product product) {

    if (!isAdmin()) {
      return noPermission();
    }

    Product updatedProduct = productRepository.save(product);

    return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
  }

  boolean isAdmin() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = userDetailsService.isAdmin(auth.getName());
    return isAdmin;
  }

  ResponseEntity<?> noPermission() {
    RequestResponse requestResponse = new RequestResponse("You don't have permission", "PERMISSION_DENIED",
        HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(requestResponse, HttpStatus.BAD_REQUEST);
  }

  ResponseEntity<?> badRequest(String code, String message, int statusCode, HttpStatus httpCode) {
    RequestResponse requestResponse = new RequestResponse(message, code, statusCode);
    return new ResponseEntity<>(requestResponse, httpCode);
  }
}
