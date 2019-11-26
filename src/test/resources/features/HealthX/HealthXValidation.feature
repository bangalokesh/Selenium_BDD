#Keywords Summary :

Feature: HealthX Validation
    
@e2eTest @HealthXMemberValidation
  Scenario: 36102 ~ 33015
    Given I Launch the HealthX Member Portal
    Then I validate the Member data
    
@e2eTest @HealthXProviderValidation
  Scenario: 36102 ~ 33015
    Given I Launch the HealthX Provider Portal
    Then I validate the Provider data