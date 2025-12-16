package com.mycom.myapp.auth.service;

import org.springframework.stereotype.Service;

import com.mycom.myapp.domain.admin.User;


public interface UserService {

    public User findUserById(Long id);

}
