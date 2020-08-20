package com.telstra.codechallenge.search.repositories.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRepositoriesResponse {

  private List<Items> items;

}
