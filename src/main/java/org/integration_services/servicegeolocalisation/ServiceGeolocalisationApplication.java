package org.integration_services.servicegeolocalisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServiceGeolocalisationApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String dbUri = dotenv.get("MONGO_DB_URI");
        System.setProperty("spring.data.mongodb.uri", dbUri);
        SpringApplication.run(ServiceGeolocalisationApplication.class, args)
        ;
    }

}
