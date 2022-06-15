# OURBOOKS PROJECT

# OURBOOKS IN JAVA REST API 

# CONFIG
You will need to create a /src/main/resources/application.properties file with this:
````
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.datasource.testWhileIdle=true
spring.datasource.validationQuery=SELECT 1
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImproveNamingStrategy
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

ourbooks.jwt.secret=[YOUR_SECRET_AS_A_LONG_LIKE_REALLY_LONG_STRING]
ourbooks.jwt.expiration=[TIME_TIL_A_NEW_AUTH_IS_REQUIRED_SUGESTION:86400000]
````
# RUN