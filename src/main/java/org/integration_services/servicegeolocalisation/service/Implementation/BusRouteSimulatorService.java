package org.integration_services.servicegeolocalisation.service.Implementation;


import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.integration_services.servicegeolocalisation.Entity.BusPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusRouteSimulatorService {
    @Autowired
    private BusWebSocketServiceImpl webSocketService;

    // Différentes vitesses pour les bus
    private static final double SPEED_SLOW = 1.0;    // km/h
    private static final double SPEED_NORMAL = 5.0;  // km/h
    private static final double SPEED_FAST = 5.0;    // km/h
    private static final double UPDATE_INTERVAL_MS = 2000;

    // État pour chaque bus
    private Map<String, BusState> busStates = new HashMap<>();

    // Définition des différentes routes
    private final Map<String, List<GpsPoint>> routes = new HashMap<>();

    @PostConstruct
    public void init() {
        // Initialisation des routes
        initializeRoutes();
        // Initialisation des bus
        initializeBuses();
    }

    private void initializeRoutes() {
        // Route 1: Hay Riad - Agdal
        routes.put("ROUTE1", List.of(
                new GpsPoint(34.0016, -6.8463, "Hay Riad Station"),
                new GpsPoint(34.0027, -6.8448, "Avenue Annakhil"),
                new GpsPoint(34.0045, -6.8426, "Carrefour des FAR"),
                new GpsPoint(34.0067, -6.8401, "Boulevard Mehdi Benbarka"),
                new GpsPoint(34.0089, -6.8378, "Place Olof Palme"),
                new GpsPoint(34.0112, -6.8356, "Avenue Hassan II"),
                new GpsPoint(34.0134, -6.8334, "Agdal Station")
        ));

        // Route 2: Témara - Centre Ville
        routes.put("ROUTE2", List.of(
                new GpsPoint(33.9252, -6.9063, "Témara Station"),
                new GpsPoint(33.9385, -6.8923, "Massira"),
                new GpsPoint(33.9522, -6.8812, "Hay Ryad"),
                new GpsPoint(33.9687, -6.8701, "Souissi"),
                new GpsPoint(33.9812, -6.8534, "Centre Ville")
        ));

        // Route 3: Salé - Rabat Centre
        routes.put("ROUTE3", List.of(
                new GpsPoint(34.0505, -6.7997, "Salé Ville Nouvelle"),
                new GpsPoint(34.0412, -6.8123, "Bab Lamrissa"),
                new GpsPoint(34.0320, -6.8245, "Avenue Mohammed V"),
                new GpsPoint(34.0234, -6.8367, "Rabat Ville"),
                new GpsPoint(34.0178, -6.8412, "Bab El Had")
        ));
    }

    private void initializeBuses() {
        // Initialiser plusieurs bus sur différentes routes
        createBus("RBT001", "ROUTE1", SPEED_NORMAL);
        createBus("RBT002", "ROUTE1", SPEED_SLOW);
        createBus("RBT003", "ROUTE2", SPEED_FAST);
        createBus("RBT004", "ROUTE2", SPEED_NORMAL);
        createBus("RBT005", "ROUTE3", SPEED_NORMAL);
        createBus("RBT006", "ROUTE3", SPEED_SLOW);
    }

    private void createBus(String busId, String routeId, double speed) {
        busStates.put(busId, new BusState(0, true, routeId, speed));
    }

    @Scheduled(fixedRate = 2000)
    public void simulateAllRoutes() {
        for (Map.Entry<String, BusState> entry : busStates.entrySet()) {
            String busId = entry.getKey();
            BusState state = entry.getValue();
            simulateBusMovement(busId, state);
        }
    }

    private void simulateBusMovement(String busId, BusState state) {
        List<GpsPoint> route = routes.get(state.routeId);
        GpsPoint current = route.get(state.currentIndex);
        GpsPoint next = state.forward ?
                route.get(Math.min(state.currentIndex + 1, route.size() - 1)) :
                route.get(Math.max(state.currentIndex - 1, 0));

        String direction = calculateDirection(current, next);

        BusPosition position = new BusPosition();
        position.setBusId(busId);
        position.setLatitude(current.lat);
        position.setLongitude(current.lon);
        position.setSpeed(state.speed);
        position.setDirection(direction);
        position.setTimestamp(System.currentTimeMillis());

        webSocketService.broadcastBusPosition(position);

        updateBusState(state, route.size());
    }

    private void updateBusState(BusState state, int routeSize) {
        if (state.forward) {
            if (state.currentIndex >= routeSize - 1) {
                state.forward = false;
                state.currentIndex--;
            } else {
                state.currentIndex++;
            }
        } else {
            if (state.currentIndex <= 0) {
                state.forward = true;
                state.currentIndex++;
            } else {
                state.currentIndex--;
            }
        }
    }

    @Data
    @AllArgsConstructor
    private static class GpsPoint {
        private double lat;
        private double lon;
        private String name;

        // public GpsPoint(double lat, double lon, String name) {
        //     this.lat = lat;
        //     this.lon = lon;
        //     this.name = name;
        // }
    }

    @Data
    @AllArgsConstructor
    private static class BusState {
        private int currentIndex;
        private boolean forward;
        private String routeId;
        private double speed;

        // public BusState(int currentIndex, boolean forward, String routeId, double speed) {
        //     this.currentIndex = currentIndex;
        //     this.forward = forward;
        //     this.routeId = routeId;
        //     this.speed = speed;
        // }
    }
    private String calculateDirection(GpsPoint current, GpsPoint next) {
        double bearing = Math.atan2(
                next.lon - current.lon,
                next.lat - current.lat
        );
        double degrees = Math.toDegrees(bearing);
        if (degrees < 0) {
            degrees += 360;
        }

        if (degrees >= 337.5 || degrees < 22.5) return "NORTH";
        if (degrees >= 22.5 && degrees < 67.5) return "NORTHEAST";
        if (degrees >= 67.5 && degrees < 112.5) return "EAST";
        if (degrees >= 112.5 && degrees < 157.5) return "SOUTHEAST";
        if (degrees >= 157.5 && degrees < 202.5) return "SOUTH";
        if (degrees >= 202.5 && degrees < 247.5) return "SOUTHWEST";
        if (degrees >= 247.5 && degrees < 292.5) return "WEST";
        return "NORTHWEST";
    }
}