CONDUITAPITEST

The goal for this project was to create playground for learning API testing.

All tests are ran against CONDUIT API which can be found here:
https://conduit.productionready.io/api

Documentation for this project can be found here: 
https://github.com/gothinkster/realworld/tree/master/api

If you want to check application UI, you can go here:  
http://angular.realworld.io/

Technologies used:
- RestAssured
- JUnit
- Allure
- Hamcrest

If you want to run tests in Maven and generate Allure report, use:  
`mvn clean test && mvn allure:serve`

On default report will be open in your browser.