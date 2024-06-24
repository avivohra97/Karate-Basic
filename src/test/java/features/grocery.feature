@firstTest
Feature: Grocery API

  Background: The Request Body Configuration
      # Set a configuration for the payload
    * url baseUrl

  @TestCaseKey=SCRUM-T1 @SCRUM-T1
  Scenario: Get All Products from Grocery
    Given header Content-Type = 'application/json'
    And path '/v2/pet/findByStatus'
    And param status = 'pending'
    When method get
    Then status 200
    * print response


