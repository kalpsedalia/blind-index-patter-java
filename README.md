# blind-index-pattern-java
Blind Index Pattern implementation using Java Spring Boot Application.

To run it locally,
  - Setup Mongo DB database locally.
  - Configure database - `contact`.
  - Update configuration in `/src/main/resources/application.properties`
  - Run application on http://localhost:8080

API endpoints:

  - Create Contact API - /contact/create

    Create contact using http://localhost:8080/contact/create

   ```json
    {
        "firstName": "Kalp",
        "lastName": "Sedalia",
        "email": "test@gmail.com",
        "mobile": "449824012345"
    }
   ```
   
  - Search Contact - /contact/search?email={email_address}&mobile={mobile_number}

    Search contact by Email or Mobile details
