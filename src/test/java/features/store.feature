@firstTest
Feature: Grocery API

  Background: The Request Body Configuration
      # Set a configuration for the payload
    * url baseUrl

  @TestCaseKey=SCRUM-T7 @SCRUM-T2
  Scenario: Get All Products from store
    Given header Content-Type = 'application/json'
    And path '/v2/store/inventory'
    When method get
    Then status 200
    * print response

