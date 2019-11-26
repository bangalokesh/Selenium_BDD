@guidingPreAuth
Feature: Guiding Care Pre Authorization
  
  @60537 @guidingPreAuth
  Scenario: 45467 ~ 60537
    Given Guiding Care Login Page as UMIntake
    Then I create preAuthorization activity
    Then Decisions on preauthorization activities
    
