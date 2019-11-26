#Keywords Summary :
@callidusTest
Feature: Callidus Customer/Member Validation
 
@60747 @callidusTest
  Scenario: 40279 ~ 60747
    Given I am on Callidus Login Screen
    Then I validate Customer Agent Details
    
    
@60747 @callidusTest 
  Scenario: 40279 ~ 60747
    Given I am on Callidus Login Screen
    When I search for Customer
    Then I validate Agent Details
    
        
@60747 @callidusAccessDB
  Scenario: 40279 ~ 60747
    Given I am on Callidus Login Screen
    Then I validate Customer Agent Details accessDB
    

