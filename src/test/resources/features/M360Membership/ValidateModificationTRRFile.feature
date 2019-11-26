#Keywords Summary :
@validateModificationTRRFile
Feature: Create Modifications TRR File to mock CMS response 
  
Background:
  Given I Open Browser
    
 @50438 @validateModificationTRRFile
  Scenario: 54139 ~ 50438
     Given I Login to MThreeSixty
    Then I verify Disenrollment
    Then I verify name change
    Then I verify change in beneficiaryID
    
  @50438 @verifyModificationLetters
	Scenario: 54139 ~ 50438
		Given I Login to MThreeSixty
		Then I navigate to MemberPage
		Then I validate NOTICE FOR DISENROLLMENT DUE TO CONFIRMATION OF OOA
		Then I validate CONFIRM VOLUNTARY DISENROLLMENT FOLLOWING RECEIPT OF TRR
		Then I validate NOTIFICATION OF PLAN PREMIUM AMOUNT DUE FOR REINSTATEMENT
		Then I validate DISEMROLL DUE TO LOSS OF PART A-B
		Then I validate MA MODEL NOTICE TO RESERARCH POTENTIAL OUT OF AREA STATUS
		Then I validate MODEL NOTICE OF DISENROLLMENT DUE TO DEATH    
		Then I close the member page