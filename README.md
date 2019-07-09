# Ecommerce.API

*Add this to `main/resources/application.properties` and change password and username* 
```spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=ecommerce
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql = true
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.SQLServer2012Dialect
spring.jpa.hibernate.ddl-auto = update
server.servlet.context-path=/api
jwt.secret=THISISA"V3RY"S3CRET$STRING*!