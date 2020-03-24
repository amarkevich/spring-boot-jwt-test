# Test configuration

```java
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:${server.port}/jwks.json",
        ...
    },
    ...
)
```

# Application security configuration

```java
@EnableWebSecurity
public class ApplicationWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/*.json");
    }
    ...
```

# Maven dependency

```xml
<dependency>
    <groupId>com.wedextim</groupId>
    <artifactId>spring-boot-jwt-test</artifactId>
    <version>0.1.1</version>
    <scope>test</scope>
</dependency>
```
