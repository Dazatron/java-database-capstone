Section 1: Architecture summary

Java Spring Boot Application (MVC & REST controllers)

Tier 1 - Presentation Layer
Thymeleaf templates are used for the Admin and Doctor dashboards.
REST API's serve other modules.


Tier 2 - Application Layer
Controllers - All controllers route requests through a common service layer
Service - Delegates to the appropriate repositories
Repositories - The application interacts with two databases


Tier 3 - Database Layer
MySQL - For patient, doctor, appointment, and admin data
MongoDB - For prescriptions
MySQL uses JPA entities while MongoDB uses document models



Section 2: Numbered flow of data and control
1. Uses access dashboards (Admin or Doctor) or APIs
2. This goes to Thymeleaf Controllers that call the service layer or REST controller
3. Service layer applies business logic  and coordinates workflows
4. The service layer communicates with the Repository Layer to perform data access operations
5. Each repository interfaces directly with the underlying database engine
6. Once data is retrieved from the database, it is mapped into Java model classes that the application can work with
7. The bound models are used in the response layer