package com.sofka.prueba.cuenta_movimiento.config;

import com.sofka.prueba.cuenta_movimiento.dto.ClienteDto;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_CLIENTE = "cliente.exchange";
    public static final String ROUTING_KEY = "cliente.creado";
    public static final String QUEUE_CLIENTE = "cliente.queue";

    @Bean
    public Queue clienteQueue() {
        return new Queue(QUEUE_CLIENTE, true);
    }

    @Bean
    public TopicExchange clienteExchange() {
        return new TopicExchange(EXCHANGE_CLIENTE);
    }

    @Bean
    public Binding bindingCliente() {
        return BindingBuilder.bind(clienteQueue()).to(clienteExchange()).with(ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.sofka.prueba.clienteservice.dto.ClienteDto", ClienteDto.class);
        classMapper.setIdClassMapping(idClassMapping);
        converter.setClassMapper(classMapper);
        return converter;
    }

}

