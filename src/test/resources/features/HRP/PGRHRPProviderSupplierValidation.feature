#Keywords Summary :
@hrp_pgr_validation
Feature: PGR to HRP Provider Supplier Validation
  
  @hrp_pgr_validation_mdh @hrp_pgr_validation @59919
  Scenario: 39782 ~ 59919
    Given I am on HRP Login Screen
    When Validate provider, supplier in HRP for PGR MDH program data
        
  @hrp_pgr_validation_mmh @59919 @hrp_pgr_validation
  Scenario: 39782 ~ 59919
    Given I am on HRP Login Screen
    When Validate provider, supplier in HRP for PGR MMH program data
    
  @hrp_pgr_validation_mph @59919 @hrp_pgr_validation
  Scenario: 39782 ~ 59919
    Given I am on HRP Login Screen
    When Validate provider, supplier in HRP for PGR MPH program data
    
  @hrp_pgr_validation_mpp @59919 @hrp_pgr_validation
  Scenario: 39782 ~ 59919
    Given I am on HRP Login Screen
    When Validate provider, supplier in HRP for PGR MPP program data
    