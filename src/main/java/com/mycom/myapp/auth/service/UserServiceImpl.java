package com.mycom.myapp.auth.service;
import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.domain.admin.User;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
