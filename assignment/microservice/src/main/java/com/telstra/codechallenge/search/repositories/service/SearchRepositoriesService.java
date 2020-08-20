package com.telstra.codechallenge.search.repositories.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public interface SearchRepositoriesService {

  public ResponseEntity<JsonNode> fetchHottestRepositories(int count);

}
