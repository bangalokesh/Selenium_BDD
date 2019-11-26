$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/resources/features/M360Membership/m360Membership.feature");
formatter.feature({
  "name": "M350 Member Eligibility Check",
  "description": "",
  "keyword": "Feature",
  "tags": [
    {
      "name": "@e2eTest"
    }
  ]
});
formatter.background({
  "name": "",
  "description": "",
  "keyword": "Background"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "I Open Browser",
  "keyword": "Given "
});
formatter.match({
  "location": "BaseStepDefinition.openBrowser()"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "4535 ~ 4529",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@e2eTest"
    },
    {
      "name": "@4535"
    },
    {
      "name": "@e2eTest"
    },
    {
      "name": "@4529"
    }
  ]
});
formatter.step({
  "name": "I Login to MThreeSixty",
  "keyword": "Given "
});
formatter.match({
  "location": "M360MembershipStepDefinition.m360Login()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});