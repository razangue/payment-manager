package com.raz.payment.infrastructure.kafka.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.raz.payment.domain.interfaces.NotificationMessageSender;
import com.raz.payment.infrastructure.kafka.services.producer.NotificationMessageProducer;

@Configuration
public class InfraConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /*@Bean(name = "notificationMessageSender")
    public NotificationMessageSender notificationMessageSender(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper customObjectMapper) {
        return new NotificationMessageProducer(kafkaTemplate, customObjectMapper);
    }*/

    @Bean
    public ObjectMapper customObjectMapper(@Value("${local.date.format}") String localDateFormat,
            @Value("${local.date.time.format}") String localDateTimeFormat) {
        ObjectMapper customObjectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Define custom formats
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(localDateFormat);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(localDateTimeFormat);

        // Register serializers/deserializers
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // Register the module with the ObjectMapper
        customObjectMapper.registerModule(javaTimeModule);

        // Optional: Configure ObjectMapper to avoid timestamps
        customObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return customObjectMapper;
    }
}
