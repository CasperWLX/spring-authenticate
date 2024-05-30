package com.hampus.authenticate_and_authorize.Service;

import com.hampus.authenticate_and_authorize.models.MyUser;
import com.hampus.authenticate_and_authorize.repo.IUserRepository;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService
{
    private IUserRepository repository;

    public MyUserDetailsService(IUserRepository repository){
        super();
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        MyUser user = repository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getRole());

        return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

}
