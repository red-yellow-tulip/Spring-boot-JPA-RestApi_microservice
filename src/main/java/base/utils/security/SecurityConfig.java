package base.utils.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity//(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);   // norm

       /* auth.inMemoryAuthentication()
                .withUser("user")
                .password("user")
                .authorities("ROLE_USER")
                .and()
                .withUser("admin")
                .password("admin")
                .authorities("ROLE_ADMIN");*/       // easy
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // обязательно для rest
                .authorizeRequests()
                .antMatchers("/group/**").hasRole( "ADMIN")
                .antMatchers("/student/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/**").permitAll()
                .and().httpBasic(); //formLogin();
    }
}