package com.takeitfree.auth.listener;

import com.takeitfree.auth.config.RabbitMQConfig;
import com.takeitfree.auth.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemNotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receive(String message) {
        notificationService.sendNewItemNotification(message);
    }
}
