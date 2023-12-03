package com.ibm.locationproducer.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.AuthenticationException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService extends IOException {

    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate;

    public void publishToKafkaTopic(String topic, String locData){
        UUID eventId = UUID.randomUUID();

        Message<String> kafkaMessage = MessageBuilder
                .withPayload(locData)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, "LocationEvent")
                .setHeader("EventId", eventId.toString())
                .build();

        kafkaProducerTemplate.send(topic, kafkaMessage)
                .doOnSuccess(e->Mono.just("Data published to Kafka topic"))
                .onErrorResume(e -> Mono.error( new AuthenticationException("Error while publishing to kafka topic.")))
                .subscribe();

    }
}
