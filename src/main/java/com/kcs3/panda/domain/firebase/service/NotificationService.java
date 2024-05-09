package com.kcs3.panda.domain.firebase.service;

import com.kcs3.panda.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {


    private final Map<Long, String> tokenMap = new HashMap<>();


    public void register(final Long userId, final String token) {
        tokenMap.put(userId, token);
    }

    private void createReceiveNotification(User sender, User receiver) {
        if (receiver.isLogin()) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title("POST RECEIVED")
                    .token(notificationService.getToken(receiver.getId()))
                    .message(NotificationType.POST_RECEIVED.generateNotificationMessage(sender, receiver))
                    .build();
            notificationService.sendNotification(notificationRequest);
        }
    }

    private void createTaggedNotification(User sender, List<User> receivers) {
        receivers.stream()
                .filter(User::isLogin)
                .forEach(receiver -> {
                    NotificationRequest notificationRequest = NotificationRequest.builder()
                            .title("POST TAGGED")
                            .token(notificationService.getToken(receiver.getId()))
                            .message(NotificationType.POST_TAGGED.generateNotificationMessage(sender, receiver))
                            .build();
                    notificationService.sendNotification(notificationRequest);
                });
    }


}