package com.lms.lms_backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.submission}")
    private String submissionQueue;

    @Value("${rabbitmq.queue.result}")
    private String resultQueue;

    @Bean
    public Queue submissionQueue() {
        return new Queue(submissionQueue, true); // durable = true
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(resultQueue, true); // durable = true
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Sử dụng JacksonJsonMessageConverter thay vì Jackson2JsonMessageConverter
        return new Jackson2JsonMessageConverter();
    }
}
