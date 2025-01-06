package org.integration_services.servicegeolocalisation.service;

import org.integration_services.servicegeolocalisation.Entity.BusPosition;
import java.util.List;

public interface BusTrackingService {
    BusPosition updateBusPosition(BusPosition position);
    List<BusPosition> getBusPositions(String busId);
    void deleteBusPosition(String id);
    List<BusPosition> getAllBusPositions();
}