package org.integration_services.servicegeolocalisation.Controller;

import lombok.RequiredArgsConstructor;
import org.integration_services.servicegeolocalisation.Entity.BusPosition;
import org.integration_services.servicegeolocalisation.service.BusTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BusTrackingController {

    @Autowired
    private BusTrackingService busTrackingService;

    @MessageMapping("/update-position")
    public void handlePositionUpdate(@Payload BusPosition position) {
        busTrackingService.updateBusPosition(position);
    }

    @PostMapping("/api/bus/position")
    public BusPosition updatePosition(@RequestBody BusPosition position) {
        busTrackingService.updateBusPosition(position);
        return position;
    }
}