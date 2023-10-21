package ru.ivanov.bootmvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ivanov.bootmvc.security.handler.LoginSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final LoginSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, LoginSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    //настраиваем аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//-------------------ЗАЧЕМ ?---------
                .authorizeRequests()
                .antMatchers("/api/admin", "/api/admin/**").hasRole("ADMIN")
                .antMatchers("/login", "/registration", "/error").permitAll()
//                .antMatchers("/**").permitAll()///////

                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and();
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .successHandler(successHandler)
                .and();

    }
}
