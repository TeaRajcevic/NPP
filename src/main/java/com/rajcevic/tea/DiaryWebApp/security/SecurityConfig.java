package com.rajcevic.tea.DiaryWebApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/register").anonymous()
                .antMatchers("/gallery").permitAll()
                .antMatchers("/search").permitAll()
                .antMatchers("/edit").permitAll()
                .anyRequest().authenticated()
                // ignore CSRF protection and allow IFRAME for h2 console
                .and()
                //.csrf().ignoringAntMatchers("/h2-console/**")
                .csrf().disable()
                //.and()
                .headers().frameOptions().sameOrigin()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/image/new", true)
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout");

    }

}