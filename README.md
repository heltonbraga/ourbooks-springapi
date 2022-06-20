# OURBOOKS PROJECT
OurBooks is an app where readers can share their books with other readers.
You can make a list of the books you have avaiable and a list of the books you wish to read, then you can check if any other reader have one of your wished books in their avaiable list, ordered by distance of their share places (where go frequently like work, school, gym...), so you can propose a meeting to get the book.
The purpose is that books are shared without costs, no money changes hands, only books.

Behind the curtains, OurBooks is the project I chose to be the common theme among some implementations in diferent languages and frameworks as I learn them.

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
