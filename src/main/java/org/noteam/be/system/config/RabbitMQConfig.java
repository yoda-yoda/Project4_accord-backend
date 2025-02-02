package org.noteam.be.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue chatQueue() {
        return new Queue("chat.queue", true);
    }

    @Bean
    public Queue aiResponseQueue() {
        return new Queue("ai.response.queue", true);
    }
}
