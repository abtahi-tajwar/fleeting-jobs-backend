package com.fleetingtrails.fleetingjobsbackend.common.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String REQUEST_JOB_DETAILS_QUEUE = "request.job_details";
    public static final String RECEIVE_JOB_DETAILS_QUEUE = "receive.job_details";

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue scrapeJobDetailsQueue() {
        return new Queue(REQUEST_JOB_DETAILS_QUEUE, true);
    }
    @Bean
    public Queue receiveJobDetailsQueue() {
        return new Queue(RECEIVE_JOB_DETAILS_QUEUE, true);
    }

    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;

    }
}
