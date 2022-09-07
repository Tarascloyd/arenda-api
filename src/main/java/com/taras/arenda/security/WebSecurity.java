package com.taras.arenda.security;

import com.taras.arenda.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static final String USERS_URL_PATH = "/api/v1/users/**";

    private static final String CITIES_URL_PATH = "/api/v1/cities";
    private static final String LOGIN_URL_PATH = "/api/v1/users/login";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/email/{email}").authenticated()
                .antMatchers(HttpMethod.POST, USERS_URL_PATH).permitAll()
                .antMatchers(HttpMethod.GET, USERS_URL_PATH).permitAll()
                .antMatchers(HttpMethod.GET, CITIES_URL_PATH).permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, authenticationManager());
        authenticationFilter.setFilterProcessesUrl(LOGIN_URL_PATH);
        authenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
