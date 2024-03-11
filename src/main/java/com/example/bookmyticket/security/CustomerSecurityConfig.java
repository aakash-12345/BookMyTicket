package com.example.bookmyticket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class CustomerSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        HeaderWriterLogoutHandler clearSiteData =
//                new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive
//                .CACHE));
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/bookMyTicket/**")).authenticated())
                .logout().logoutUrl("/bookMyTicket/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
//                .addLogoutHandler(clearSiteData)
                .and()
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
