package com.mycom.myapp.domain.user.service;

import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.user.UserRepository;
import com.mycom.myapp.domain.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUserById(Long id) {
        Optional<User> optionalFindUser = userRepository.findById(id);
        return optionalFindUser.orElseThrow(()-> new RuntimeException());
    }

}
