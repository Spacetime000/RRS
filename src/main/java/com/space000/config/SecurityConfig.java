package com.space000.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user1 = User.withUsername("user1")
//                .password(passwordEncoder().encode("pass"))
//                .roles("USER")
//                .build();
//        UserDetails admin1 = User.withUsername("admin1")
//                .password(passwordEncoder().encode("pass"))
//                .roles("admin")
//                .build();
//        return new InMemoryUserDetailsManager(user1, admin1);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**", "/", "/files/**", "/notice/board","/notice/dtl/**", "/notice/fileDown/**", "/members/**").permitAll()
                .mvcMatchers("/review/**", "/shop/**").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/banner/**", "/notice/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
