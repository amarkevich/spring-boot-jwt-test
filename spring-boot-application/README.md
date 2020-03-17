1. Amazon Cognito
java -Dregion=region -DcognitoPoolId=cognitoPoolId -jar target/spring-boot-application-0.0.1-SNAPSHOT.jar --spring.profiles.active=cognito
2. Apache CXF Fediz
java -Djavax.net.ssl.trustStore=jks/ststrust.jks -Djavax.net.ssl.trustStorePassword=storepass -jar target/spring-boot-application-0.0.1-SNAPSHOT.jar --spring.profiles.active=fediz
3. Keycloak
java -jar target/spring-boot-application-0.0.1-SNAPSHOT.jar --spring.profiles.active=keycloak
