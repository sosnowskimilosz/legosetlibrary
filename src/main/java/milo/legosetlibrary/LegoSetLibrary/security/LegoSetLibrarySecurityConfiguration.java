package milo.legosetlibrary.LegoSetLibrary.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class LegoSetLibrarySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/legosets/**").permitAll()
                .anyRequest().authenticated()
        .and()
                .httpBasic()
        .and()
            .csrf().disable();
    }


}
