package com.takeitfree.auth.service.impl;

import com.takeitfree.auth.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNewItemNotification(String message) {
        /// /topic/new-items → all clients subscribed to this topic will receive the message.
        messagingTemplate.convertAndSend("/topic/new-items", message);
    }
}
