package com.invManagement.Backend_REST_API.services;

import com.invManagement.Backend_REST_API.models.MyUser.MyUser;
import com.invManagement.Backend_REST_API.models.MyUser.MyUserRepository;
import com.invManagement.Backend_REST_API.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final MyUserRepository repository;

    public MyUserDetailService(MyUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            MyUser userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(MyUser user) {
        if (user.getRole() == Role.EMPLOYEE) {
            return new String[]{"EMPLOYEE"};
        }
        else if (user.getRole() == Role.MANAGER) {
            return new String[]{"MANAGER", "EMPLOYEE"};
        }
        return new String[]{"NOROLE"};
    }


}
