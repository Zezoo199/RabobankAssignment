## Rabobank Assignment for Authorizations Area

Spring Boot REST API For PowerOfAttorney Operations.

### Getting Started

#### Running tests

mvn clean install

#### Starting the server

Start as SpringBoot app from RaboAssignmentApplication.java

#### Swagger

Navigate to http://localhost:8080/swagger-ui.html then view/test the endpoints

### Built with

Spring boot 2.5.1,MongoDB,Maven,REST,Google code formatting and Swagger OpenAPI.

### Test coverage

Test coverage for used lines is 100%

### Background information

A Power of Attorney is used when someone (grantor) wants to give access to his/her account to
someone else (grantee). This could be read access or write access. In this way the grantee can
read/write in the grantors account. Notice that this is a simplified version of reality.

### Assumptions and Questions

* Assume Accounts exists (Upload example accounts in Main class)
* Use of domain module in MongoDB layer without creation of new DO layer.
* Create new DTO layer when domain can't be used otherwise use Domain.
* Assume Frontend Exists.