package com.kcs3.panda.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kcs3.panda.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserNickname(String userNickname);  // 닉네임으로 사용자 찾기
}
