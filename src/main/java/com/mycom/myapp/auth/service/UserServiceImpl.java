<<<<<<<< HEAD:src/main/java/com/mycom/myapp/domain/user/service/UserServiceImpl.java
package com.mycom.myapp.domain.user.service;

import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.user.UserRepository;
import com.mycom.myapp.domain.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
========
package com.mycom.myapp.auth.service;

>>>>>>>> 2c3763e935f635bff5ccd5ab27ee909ae30c7ee0:src/main/java/com/mycom/myapp/auth/service/UserServiceImpl.java
import org.springframework.stereotype.Service;

import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.domain.User;

import lombok.RequiredArgsConstructor;

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
