#Keywords Summary :
@e2eTest
Feature: M360 Member Payment Check
  
 Background:
   Given I Open Browser

    
@60681 @validateMemberPayments
  Scenario: 39997 ~ 60681
    Given I Login to MThreeSixty
    When Navigate to the Billing page
    And Navigate to the Member payment tab
    Then validate the Member payments details
    
 @60670 @beforepaymentinvoiceValidation
  Scenario: 39997 ~ 60670   
   	Given I Login to MThreeSixty
    When Navigate to the Billing page
    Then I Search and verify open Invoice Details for each Member
    
 @60682 @AfterPaymentinvoiceValidation
   Scenario: 39997 ~ 60682   
   	Given I Login to MThreeSixty
    When Navigate to the Billing page
    Then I Search and verify open Invoice Details for each Member
    
 @60682 @AfterPaymentinvoiceValidation
  Scenario: 39997 ~ 60682   
   	Given I Login to MThreeSixty
    When Navigate to the Billing page
    Then I Search and verify Unapplied Cash Invoice Details for each Member 
    
    