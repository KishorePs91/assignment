package com.telstra.codechallenge.search.repositories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.telstra.codechallenge.search.repositories.util.SearchRepositoriesConstants;
import com.telstra.codechallenge.search.repositories.util.SearchRepositoriesUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SearchRepositoriesServiceTest {

  @Mock
  private SearchRepositoriesUtil searchRepositoriesUtil;

  @Mock
  private RestTemplate restTemplate;
  
  @Mock
  HttpEntity<HttpHeaders> requestEntity;
  
  @Value("${github.api.prefix}")
  private String gitHubApiPrefix;

  @Value("${github.api.search.repositories.endpoint}")
  private String searchRepositoriesEndpoint;

  @Value("${github.api.param.qualifier.value}")
  private String qualifierValue;

  @Value("${github.api.param.sort.value}")
  private String sortValue;

  @Value("${github.api.param.order.value}")
  private String ordervalue;

  @Value("${github.api.param.perpage.value}")
  private int perPageValue;
  
  @InjectMocks
  private SearchRepositoriesService searchRepositoriesServiceImpl = new SearchRepositoriesServiceImpl();
  
  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(searchRepositoriesServiceImpl, "gitHubApiPrefix", "https://api.github.com");
    ReflectionTestUtils.setField(searchRepositoriesServiceImpl, "searchRepositoriesEndpoint", 
    		"/search/repositories");
    ReflectionTestUtils.setField(searchRepositoriesServiceImpl, "qualifierValue", "created:>");
    ReflectionTestUtils.setField(searchRepositoriesServiceImpl, "sortValue", "stars");
    ReflectionTestUtils.setField(searchRepositoriesServiceImpl, "ordervalue", "desc");
    ReflectionTestUtils.setField(searchRepositoriesServiceImpl, "perPageValue", 100);
    
    ResponseEntity<JsonNode> mockedJSON = SearchRepositoriesUtil.getSearchRepositoriesJSON();
    
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime now = LocalDateTime.now();
    String then = now.minusDays(7).format(format);
    
    HttpHeaders header = new HttpHeaders();
    header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
    header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
    
    HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(header);
    
    URI gitAPIUrl = UriComponentsBuilder.fromHttpUrl(gitHubApiPrefix)
      .path(searchRepositoriesEndpoint)
      .queryParam(SearchRepositoriesConstants.QUALIFIER, qualifierValue+then)
      .queryParam(SearchRepositoriesConstants.SORT, sortValue)
      .queryParam(SearchRepositoriesConstants.ORDER, ordervalue)
      .queryParam(SearchRepositoriesConstants.PERPAGE, 1)
      .queryParam(SearchRepositoriesConstants.PAGE, 1)
      .build().encode().toUri();  

    when(restTemplate.exchange(gitAPIUrl, HttpMethod.GET, requestEntity, JsonNode.class))
      .thenReturn(mockedJSON);
  }
  
  @Test
  public void getHottestRepositoriesTest() throws Exception {

    ResponseEntity<JsonNode> expectedResponse = SearchRepositoriesUtil.getRepositoriesDetails();
    ResponseEntity<JsonNode> mockedJSON = SearchRepositoriesUtil.getSearchRepositoriesJSON();
    when(searchRepositoriesServiceImpl.fetchHottestRepositories(1))
    .thenReturn(mockedJSON);
    
    ResponseEntity<JsonNode> response = searchRepositoriesServiceImpl.fetchHottestRepositories(1);
    assertEquals(expectedResponse, response);
    
  }
  
  @Test
  public void getHttpErrorResponse() throws Exception {

    ResponseEntity<JsonNode> expectedResponse = SearchRepositoriesUtil.getHttpErrorResponse();
    when(restTemplate.exchange("", HttpMethod.GET, requestEntity, JsonNode.class))
      .thenReturn(null);

    ResponseEntity<JsonNode> response = searchRepositoriesServiceImpl.fetchHottestRepositories(1);
    assertEquals(expectedResponse, response);
    
  }
  
  @Test
  public void getErrorResponse() throws Exception {
	  
    ResponseEntity<JsonNode> expectedResponse = SearchRepositoriesUtil.getErrorResponse();
    when(searchRepositoriesServiceImpl.fetchHottestRepositories(1)).thenThrow(HttpClientErrorException
    		.create(null, HttpStatus.FORBIDDEN, "rate limit exceeded", null, null, null));

    ResponseEntity<JsonNode> response = searchRepositoriesServiceImpl.fetchHottestRepositories(1);
    assertEquals(expectedResponse, response);
    
  }
  
  @Test
  public void getJsonErrorResponse() throws Exception {
	    
    ResponseEntity<JsonNode> expectedResponse = SearchRepositoriesUtil.getRepositoriesDetails();
    ResponseEntity<JsonNode> jsonException = SearchRepositoriesUtil.getJsonErrorResponse();
    when(searchRepositoriesServiceImpl.fetchHottestRepositories(1)).thenReturn(expectedResponse);

    ResponseEntity<JsonNode> response = searchRepositoriesServiceImpl.fetchHottestRepositories(1);
    assertEquals(jsonException, response);
    
  }
}
