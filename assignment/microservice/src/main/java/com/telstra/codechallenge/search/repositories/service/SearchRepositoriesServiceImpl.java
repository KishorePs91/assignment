package com.telstra.codechallenge.search.repositories.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telstra.codechallenge.search.repositories.util.SearchRepositoriesConstants;
import com.telstra.codechallenge.search.repositories.vo.ApiError;
import com.telstra.codechallenge.search.repositories.vo.Items;
import com.telstra.codechallenge.search.repositories.vo.SearchRepositoriesResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchRepositoriesServiceImpl implements SearchRepositoriesService {

  @Autowired
  private RestTemplate restTemplate;
  
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

  @Override
  public ResponseEntity<JsonNode> fetchHottestRepositories(int count) {

    log.info("Inside fetchHottestRepositories...");
    
    ResponseEntity<JsonNode> searchRepositoriesResponse = null;
    List<Items> listOfItems = new ArrayList<Items>();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = null;
    
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime now = LocalDateTime.now();
    String then = now.minusDays(7).format(format);
    
    int countPerPage;
    int pageNo = count/100;
    int perPage = count%100;
    
    try {
      
      HttpHeaders header = new HttpHeaders();
      header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
      header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());

      HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(header);
      
      for (int i = 0; i <= pageNo; i++) {
        if (i == pageNo) {
          countPerPage = perPage;
          if (perPage == 0) {
            break;
          }
        } else {
          countPerPage = perPageValue;
        }
        
        URI gitAPIUrl = UriComponentsBuilder.fromHttpUrl(gitHubApiPrefix).path(searchRepositoriesEndpoint)
      		  .queryParam(SearchRepositoriesConstants.QUALIFIER, qualifierValue+then)
                .queryParam(SearchRepositoriesConstants.SORT, sortValue)
                .queryParam(SearchRepositoriesConstants.ORDER, ordervalue)
                .queryParam(SearchRepositoriesConstants.PERPAGE, countPerPage)
                .queryParam(SearchRepositoriesConstants.PAGE, 1 + i).build().encode().toUri();

        log.info("GitHub API call to fetch Hottest Repositories..");
        searchRepositoriesResponse = restTemplate.exchange(gitAPIUrl, HttpMethod.GET, requestEntity, 
        													JsonNode.class);
        log.info("inside {}",searchRepositoriesResponse);
        if (searchRepositoriesResponse.getStatusCode() == HttpStatus.OK) {
          log.info("Received successful response from GitHub API.");
          SearchRepositoriesResponse accountsList = mapper.treeToValue(searchRepositoriesResponse.getBody(), 
          		SearchRepositoriesResponse.class);
          listOfItems.addAll(accountsList.getItems());

        }
      }
           
      log.info("Number of Hottest Repositories retrived: {}",listOfItems.size());
      node = mapper.convertValue(listOfItems, JsonNode.class);
      
    } catch(HttpClientErrorException httpEx) {

      log.error(httpEx.getStatusText(),httpEx);
      ApiError apiError =  new ApiError(httpEx.getStatusCode(), httpEx.getLocalizedMessage(), 
    		  httpEx.getStatusText());
      return ResponseEntity.status(httpEx.getStatusCode()).body(
    		  mapper.convertValue(apiError, JsonNode.class));

    } catch(JsonProcessingException jsonEx) {

      log.error("Error occured when filtering JSONResponse.{}",jsonEx);
      ApiError apiError =  new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, jsonEx.getLocalizedMessage(), 
    		  "Error occured when filtering JSONResponse.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
    		  mapper.convertValue(apiError, JsonNode.class));

    } catch (Exception ex) {

      log.error("GitHub API call failed for getting Details.{}",ex);
      ApiError apiError =  new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), 
    		  "GitHub API call failed for getting Details.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
    		  mapper.convertValue(apiError, JsonNode.class));

    }

    return ResponseEntity.ok(node);
  }

}
