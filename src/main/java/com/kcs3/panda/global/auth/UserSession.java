package com.kcs3.panda.global.auth;


import lombok.Getter;

@Getter
public class UserSession {
    private Long id;
    private String userName;
    private String email;
    // 다른 필드들도 추가할 수 있습니다.

    public UserSession(Long id, String username, String email) {
        this.id = id;
        this.userName = username;
        this.email = email;
    }

    // Getter, Setter 등의 메서드를 추가할 수 있습니다.

    // 사용자의 권한을 설정하는 메서드도 추가할 수 있습니다.
}
