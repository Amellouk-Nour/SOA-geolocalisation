package org.integration_services.servicegeolocalisation.service.Implementation;

import lombok.RequiredArgsConstructor;
import org.integration_services.servicegeolocalisation.Entity.BusPosition;
import org.integration_services.servicegeolocalisation.Repository.BusPositionRepository;
import org.integration_services.servicegeolocalisation.service.BusTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusTrackingServiceImpl implements BusTrackingService {

    @Autowired
    private BusPositionRepository busPositionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private StreamBridge streamBridge;

    @Override
    public BusPosition updateBusPosition(BusPosition position) {
        BusPosition savedPosition = busPositionRepository.save(position);
        messagingTemplate.convertAndSend("/topic/bus/" + position.getBusId(), savedPosition);
        streamBridge.send("busPositionProducer-out-0", savedPosition);
        return savedPosition;
    }

    @Override
    public List<BusPosition> getBusPositions(String busId) {
        return busPositionRepository.findByBusId(busId);
    }

    @Override
    public void deleteBusPosition(String id) {
        busPositionRepository.deleteById(id);
    }

    @Override
    public List<BusPosition> getAllBusPositions() {
        return busPositionRepository.findAll();
    }
}