package com.hampus.authenticate_and_authorize.config;

import com.hampus.authenticate_and_authorize.AuthFiles.CustomWebAuthenticationDetailsSource;
import com.hampus.authenticate_and_authorize.AuthFiles.TwoFactorAuthenticator;
import com.hampus.authenticate_and_authorize.beans.MyBean;
import com.hampus.authenticate_and_authorize.repo.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig
{
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    public MyConfig(CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource){
        this.customWebAuthenticationDetailsSource = customWebAuthenticationDetailsSource;
    }

    @Bean
    public MyBean myBean(){
        return new MyBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, IUserRepository repository) {
        return new TwoFactorAuthenticator(repository, userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity, DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/register"))
                .authorizeHttpRequests(auth -> auth
            .requestMatchers("/register").permitAll()
            .requestMatchers("/home").permitAll()
            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/admin/**").hasRole("ADMIN"))
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .authenticationDetailsSource(customWebAuthenticationDetailsSource)
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
