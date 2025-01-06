package org.integration_services.servicegeolocalisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServiceGeolocalisationApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // MongoDB Configuration
        System.setProperty("spring.data.mongodb.uri", dotenv.get("MONGO_DB_URI"));
        System.setProperty("spring.data.mongodb.username", dotenv.get("MONGO_DB_USERNAME"));
        System.setProperty("spring.data.mongodb.password", dotenv.get("MONGO_DB_PASSWORD"));

        // Kafka Configuration
        System.setProperty("spring.cloud.stream.kafka.binder.brokers", dotenv.get("KAFKA_ADVERTISED_LISTENERS"));

        SpringApplication.run(ServiceGeolocalisationApplication.class, args);
    }
}