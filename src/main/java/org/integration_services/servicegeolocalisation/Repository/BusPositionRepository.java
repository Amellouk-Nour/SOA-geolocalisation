package org.integration_services.servicegeolocalisation.Repository;


import org.integration_services.servicegeolocalisation.Entity.BusPosition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BusPositionRepository extends MongoRepository<BusPosition, String> {
    List<BusPosition> findByBusId(String busId);
}