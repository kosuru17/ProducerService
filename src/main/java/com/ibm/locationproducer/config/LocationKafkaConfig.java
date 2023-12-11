package com.ibm.locationproducer.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class LocationKafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(){
        return new ReactiveKafkaProducerTemplate<>(createSender());
    }

    @Bean
    public KafkaSender<String, String> createSender() {
        Map<String, Object> producerProps = new HashMap<>();

        // Required Confluent Cloud Configuration
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-4r087.us-west2.gcp.confluent.cloud:9092");
        producerProps.put("schema.registry.url", "https://psrc-q8qx7.us-central1.gcp.confluent.cloud");
        producerProps.put("basic.auth.credentials.source", "USER_INFO");
        producerProps.put("schema.registry.basic.auth.user.info", "S5HL45TLP7JT6BXB:DLcIqumrpuXpUIOc1wdYvy6pvCdKHJ6n+RMRBArlUHityzLdPl5PtSrZo9J7JK7o");

        // General Producer Configuration
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.ACKS_CONFIG, "all");


        // SASL_SSL Security Configuration
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        producerProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        producerProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"2Q33U66OQW2BNHP5\" password=\"WG0GZcP9TFv9SpmJbbkGDJvFQKWXGfgCXTiqjBFTJU0p8ifZCBQEguTOIez7DDsW\";");

        SenderOptions<String, String> senderOptions = SenderOptions.create(producerProps);

        return KafkaSender.create(senderOptions);
    }
}
