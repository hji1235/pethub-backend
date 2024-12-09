package com.hji1235.pethub.user.service;

import com.hji1235.pethub.user.dto.UserJoinRequest;
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
        String username = request.getUsername();
        String password = request.getPassword();

        boolean isExits = userRepository.existsByUsername(username);

        if (isExits) {
            return;
        }

        User user = new User(username, passwordEncoder.encode(password), "ROLE_ADMIN");
        userRepository.save(user);
    }
}
