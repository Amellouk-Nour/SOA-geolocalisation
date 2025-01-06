package org.integration_services.servicegeolocalisation.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "bus_positions")
@AllArgsConstructor@NoArgsConstructor
public class BusPosition {
    @Id
    private String id;
    private String busId;
    private double latitude;
    private double longitude;
    private long timestamp;
    private double speed;
    private String direction;
}
