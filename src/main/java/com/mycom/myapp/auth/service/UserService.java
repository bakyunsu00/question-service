package com.mycom.myapp.auth.service;

import com.mycom.myapp.domain.User;
import org.springframework.stereotype.Service;


public interface UserService {

    public User findUserById(Long id);

}
