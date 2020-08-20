package com.telstra.codechallenge.search.repositories.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {

  private String language;
  private String description;
  private String name;
  @JsonProperty("html_url")
  private String htmlurl;
  @JsonProperty("watchers_count")
  private int watcherscount;

}
