#Keywords Summary :
@oecFileGen
Feature: M360 OEC File Member Enrollment
  
 Background:
   Given I Open Browser
    
@51039 @oecFileGen
  Scenario: 32992 ~ 51039
    Given I Login to MThreeSixty
    Then I navigate to EligibilityPage
    Then I create OECFile
    Then I clean up OEC table for next run