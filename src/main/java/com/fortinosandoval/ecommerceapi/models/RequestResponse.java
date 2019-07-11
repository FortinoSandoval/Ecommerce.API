package com.fortinosandoval.ecommerceapi.models;

/**
 * BadRequestError
 */
public class RequestResponse {
  public String message;
  public String code;
  public int statusCode;

  public RequestResponse(String message, String code, int statusCode) {
    super();
    this.message = message;
    this.code = code;
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message){
    this.message = message;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code){
    this.code = code;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public void setStatusCode(int statusCode){
    this.statusCode = statusCode;
  }
  
}