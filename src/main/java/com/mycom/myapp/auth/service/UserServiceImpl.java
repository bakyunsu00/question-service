package com.mycom.myapp.auth.service;

import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUserById(Long id) {
        User findUser = userRepository.findById(id).get();
        return findUser;
    }

}
