package com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer;

import com.fleetingtrails.fleetingjobsbackend.common.config.RabbitConfig;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducerService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }

    public void requestJobDetails(RequestJobDetailsMessageDto message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.REQUEST_JOB_DETAILS_QUEUE,
                message
        );
    }
}
