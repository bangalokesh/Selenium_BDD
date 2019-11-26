#Keywords Summary :

Feature: M360 Membership
    
 @4535 @e2eTest @1101
  Scenario: 1000 ~ 1101
    Given I Login to MThreeSixty
 	Then I do eligibility check
    
@4535 @e2eTest @eligCheck
  Scenario: 3400 ~ 3600
    Given I Login to MThreeSixty
    Then I do eligibility check
    
@4535 @e2eTest @appStatus
  Scenario: 3400 ~ 3500
    Given I Login to MThreeSixty
    Then I verify application status 
    
@4535 @e2eTest @4529 @letterValidation
  Scenario: 36102 ~ 33015
    Given I Login to MThreeSixty
    Then I verify letter details
    
@60777 @MigratedMemValidation
  Scenario: 3100 ~ 60777
    Given I validate Migrated Member Data in M360

@e2etest @updateCVT_MedicareID
  Scenario: 35603
    Given I Login to MThreeSixty
    Then I navigate to MemberPage
    Then I update CVT_MemberData table with MedicareID
    
