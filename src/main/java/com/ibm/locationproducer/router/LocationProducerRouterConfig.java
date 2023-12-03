package com.ibm.locationproducer.router;

import com.ibm.locationproducer.handler.LocationProducerHandler;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class LocationProducerRouterConfig {

    private final LocationProducerHandler producerHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(LocationProducerHandler producerHandler){
        return route().path(
                "/router", builder ->
                builder.POST("/publishToKafka",producerHandler::readAndPublishToKafka))
                .build();
    }
}





