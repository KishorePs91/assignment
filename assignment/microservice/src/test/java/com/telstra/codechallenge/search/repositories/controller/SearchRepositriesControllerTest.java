package com.telstra.codechallenge.search.repositories.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.telstra.codechallenge.search.repositories.util.SearchRepositoriesUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SearchRepositriesControllerTest {

  @Mock
  private SearchRepositoriesController searchRepositriesController;

  @Test
  public void getHottestRepository() throws Exception {

    ResponseEntity<JsonNode> mockedResponse = SearchRepositoriesUtil.getRepositoriesDetails();
    when(searchRepositriesController.getHottestRepository(1)).thenReturn(mockedResponse);

    ResponseEntity<JsonNode> response = searchRepositriesController.getHottestRepository(1);
    assertEquals(mockedResponse, response);

  }

}
