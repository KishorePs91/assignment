# See
# https://github.com/intuit/karate#syntax-guide
# for how to write feature scenarios
Feature: As a developer i want to know if my spring boot application is running

  Scenario: Fetch the Hottest Repository from Git Hub
    Given url microserviceUrl
    And path '/fetch/hottest/repositories'
    And params {'count':'1'}
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    And match response == [{language: '#string',description: '#string',name: '#string',html_url: '#string',watchers_count: '#number'}]

  Scenario: Fetch 5 Hottest Repositories from Git Hub
    Given url microserviceUrl
    And path '/fetch/hottest/repositories'
    And params {'count':'5'}
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    * def quoteSchema = {language: '#string',description: '#string',name: '#string',html_url: '#string',watchers_count: '#number'}
    And match response ==  '#[5] quoteSchema'

  Scenario: Fetch zero Hottest Repositories from Git Hub
    Given url microserviceUrl
    And path '/fetch/hottest/repositories'
    And params {'count':'0'}
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    And match response ==
    """
    []
    """
    
  Scenario: Fetch all the Hottest Repositories from Git Hub
    Given url microserviceUrl
    And path '/fetch/hottest/repositories'
    And params {'count':'100'}
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    * def quoteSchema = {language: '#string',description: '#string',name: '#string',html_url: '#string',watchers_count: '#number'}
    And match response ==  '#[100] quoteSchema'
    
  Scenario: Fetch the Hottest Repositories from Git Hub with bad input
    Given url microserviceUrl
    And path '/fetch/hottest/repositories'
    And params {'count':'qwerty'}
    When method GET
    Then status 400
    And match header Content-Type contains 'application/json'
    * def quoteSchema = {language: '#string',description: '#string',name: '#string',html_url: '#string',watchers_count: '#number'}
    And match response !=  'quoteSchema'
    
  Scenario: Fetch the Hottest Repositories from Git Hub with invalid input
    Given url microserviceUrl
    And path '/fetch/hottest/repositories/count'
    When method GET
    Then status 404
