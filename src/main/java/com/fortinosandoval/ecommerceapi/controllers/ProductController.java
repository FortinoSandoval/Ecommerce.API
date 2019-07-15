package com.fortinosandoval.ecommerceapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.fortinosandoval.ecommerceapi.repository.ProductRepository;
import com.fortinosandoval.ecommerceapi.models.entity.Product;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "product", description = "Operations partaining to Products")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @ApiOperation(value = "View the product list", response = Iterable.class)
  @RequestMapping(value = "/products")
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
}
