package com.hji1235.pethub.user.service;

import com.hji1235.pethub.user.dto.UserJoinRequest;
import com.hji1235.pethub.user.entity.Role;
import com.hji1235.pethub.user.entity.User;
import com.hji1235.pethub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void join(UserJoinRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        boolean isExits = userRepository.existsByEmail(email);

        if (isExits) {
            return;
        }

        User user = new User(email, passwordEncoder.encode(password), Role.USER);
        userRepository.save(user);
    }
}
