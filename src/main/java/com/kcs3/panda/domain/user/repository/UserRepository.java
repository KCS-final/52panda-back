package com.kcs3.panda.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.kcs3.panda.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNickname(String nickname);  // 닉네임으로 사용자 찾기

    @Transactional(readOnly = true)  // 읽기 전용 트랜잭션
    User findByUserEmail(String email);  // 이메일로 사용자 찾기
}

