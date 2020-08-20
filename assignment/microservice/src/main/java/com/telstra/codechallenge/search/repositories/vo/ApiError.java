package com.telstra.codechallenge.search.repositories.vo;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiError {

  private HttpStatus status;
  private String message;
  private List<String> errors;

  public ApiError(HttpStatus status, String message, String error) {

    super();
    this.status = status;
    this.message = message;
    errors = Arrays.asList(error);

  }
  
}
