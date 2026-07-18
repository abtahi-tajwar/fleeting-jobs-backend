package com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.listener;

import com.fleetingtrails.fleetingjobsbackend.common.config.RabbitConfig;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveNewJobListingMessageDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.service.JobService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitListenerService {
    private final JobService jobService;

    public RabbitListenerService (JobService jobService) {
        this.jobService = jobService;
    }

//    @RabbitListener(queues = RabbitConfig.RECEIVE_JOB_DETAILS_QUEUE)
    public void receiveJobDetails (ReceiveJobDetailsMessageDto message) {
        jobService.receiveJobDetails(message);
    }

    @RabbitListener(queues = RabbitConfig.RECEIVE_NEW_JOB_LISTING)
    public void receiveNewJobListing (ReceiveNewJobListingMessageDto message) {
        jobService.handleRecieveNewJobListing(message);
    }
}
