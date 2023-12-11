package com.ibm.locationproducer.handler;

import com.ibm.locationproducer.exception.LocationApiException;
import com.ibm.locationproducer.service.AzureBlobStorageService;
import com.ibm.locationproducer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LocationProducerHandler {

    private final AzureBlobStorageService azureBlobStorageService;


    private final KafkaProducerService kafkaProducerService;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    public Mono<ServerResponse> readAndPublishToKafka(ServerRequest serverRequest) {
        try {
            Flux<String> jsonData = azureBlobStorageService.readJsonFilesFromContainer();
            Mono<String> response = jsonData.doOnNext(value -> kafkaProducerService.publishToKafkaTopic(topic, value))
                    .then(Mono.just("Data published to Kafka"))
                    .onErrorResume(e -> Mono.error(new LocationApiException("An error occurred while data processing..!!")));
            return ServerResponse.ok().body(response, String.class);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
