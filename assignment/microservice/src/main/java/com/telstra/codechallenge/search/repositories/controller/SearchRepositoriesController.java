package com.telstra.codechallenge.search.repositories.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.telstra.codechallenge.search.repositories.service.SearchRepositoriesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SearchRepositoriesController {

  @Autowired SearchRepositoriesService searchRepositoriesService;

  @GetMapping(value = "${fetch-hottest-repositories}", 
		  produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<JsonNode> getHottestRepository(@RequestParam(
		  value = "count", defaultValue = "1")int count){

    log.info("Request received in getHottestRepository()..");
    return searchRepositoriesService.fetchHottestRepositories(count);

  }
}