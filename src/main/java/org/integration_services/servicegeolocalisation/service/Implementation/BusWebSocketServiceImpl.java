package org.integration_services.servicegeolocalisation.service.Implementation;


import lombok.RequiredArgsConstructor;
import org.integration_services.servicegeolocalisation.Entity.BusPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BusWebSocketServiceImpl {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final Map<String, BusPosition> lastPositions = new ConcurrentHashMap<>();

    public void broadcastBusPosition(BusPosition position) {
        lastPositions.put(position.getBusId(), position);
        messagingTemplate.convertAndSend("/topic/bus/" + position.getBusId(), position);
        messagingTemplate.convertAndSend("/topic/buses/all", lastPositions.values());
    }
}
