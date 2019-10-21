package com.wedextim.spring.boot.application;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
// 'proxyTargetClass = true' required to avoid https://github.com/spring-projects/spring-boot/issues/18523
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/*.json", "/actuator/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors()
                .and()
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(new CognitoGroupAuthoritiesExtractor());
    }

    static class CognitoGroupAuthoritiesExtractor extends JwtAuthenticationConverter {

        @Override
        protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
            final Collection<String> authorities = jwt.getClaimAsStringList("cognito:groups");
            if (authorities == null) {
                return Collections.emptyList();
            }
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

    }

}
