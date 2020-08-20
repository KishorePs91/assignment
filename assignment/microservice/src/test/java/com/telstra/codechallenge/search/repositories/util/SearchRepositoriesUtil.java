package com.telstra.codechallenge.search.repositories.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchRepositoriesUtil {

  public static ResponseEntity<JsonNode> getRepositoriesDetails() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = "[{\"language\": \"Python\",\"description\": \"[Open Source]."
            + "  The improved version of AnimeGAN. \",\"name\": \"AnimeGANv2\",\"html_url\": "
    		+ "\"https://github.com/TachibanaYoshino/AnimeGANv2\",\"watchers_count\": 578}]";
    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
    return ResponseEntity.ok(jsonNode);

  }

  public static ResponseEntity<JsonNode> getSearchRepositoriesJSON() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = "{\"items\":[{\"language\": \"Python\",\"description\": \"[Open Source]."
    		+ "  The improved version of AnimeGAN. \",\"name\": \"AnimeGANv2\",\"html_url\": "
    		+ "\"https://github.com/TachibanaYoshino/AnimeGANv2\",\"watchers_count\": 578}]}";
    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
    return ResponseEntity.ok(jsonNode);

  }
  
  public static ResponseEntity<JsonNode> getErrorResponse() throws Exception {
	  
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = "{\"status\":\"FORBIDDEN\",\"message\":\"403 rate limit exceeded\","
    		+ "\"errors\":[\"rate limit exceeded\"]}";
    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(jsonNode);
    
  }
  
  public static ResponseEntity<JsonNode> getHttpErrorResponse() throws Exception {
	  
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = "{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":null,\"errors\""
    		+ ":[\"GitHub API call failed for getting Details.\"]}";
    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonNode);
    
  }
  
  public static ResponseEntity<JsonNode> getJsonErrorResponse() throws Exception {
	  
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = "{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"Cannot deserialize instance of "
    		+ "`com.telstra.codechallenge.search.repositories.vo.SearchRepositoriesResponse` out of START_ARRAY "
    		+ "token\\n at [Source: UNKNOWN; line: -1, column: -1]\",\"errors\":[\"Error occured when filtering "
    		+ "JSONResponse.\"]}";
    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonNode);
    
  }

}
