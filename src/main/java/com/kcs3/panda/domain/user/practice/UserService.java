package com.kcs3.panda.domain.user.practice;

import com.kcs3.panda.domain.user.entity.User;
import com.kcs3.panda.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 새로운 사용자 추가
    public User addUser(String userNickname, String userEmail) {
        // 중복 검사
        if (userRepository.findByUserEmail(userEmail) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User newUser = new User();
        newUser.setUserNickname(userNickname);
        newUser.setUserEmail(userEmail);
        newUser.setUserPoint(0); // 기본 포인트 설정

        return userRepository.save(newUser); // 사용자 저장 및 반환
    }
}