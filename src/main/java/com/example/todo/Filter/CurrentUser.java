package com.example.springdb.Filter;

import com.example.springdb.DB.JsonUserDB;
import com.example.springdb.entity.JsonUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Configuration
@RequiredArgsConstructor
public class CurrentUser implements UserDetailsService {
    private final JsonUserDB userDB;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<JsonUser> user = userDB.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }else {
            throw new UsernameNotFoundException(username);
        }
    }
}
