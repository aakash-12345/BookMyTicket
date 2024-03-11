package com.example.bookmyticket.security;

import com.example.bookmyticket.dao.Customer;
import com.example.bookmyticket.repos.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerSecurityDetails implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        String password;
        List<GrantedAuthority> authorities;
        Optional<Customer> customer = customerRepository.findByCustomerUserName(userName);
        if (!customer.isPresent()) {
            throw new UsernameNotFoundException("User details not found for the user : " + userName);
        } else {
            userName = customer.get().getCustomerUserName();
            password = customer.get().getCustomerPassword();
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(customer.get().getCustomerRoles()));
        }
        return new User(userName, password, authorities);

    }

}
