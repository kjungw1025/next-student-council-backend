package com.dku.council.domain.bus.service;

import com.dku.council.domain.bus.model.BusStation;
import com.dku.council.domain.bus.model.CachedBusArrivals;
import com.dku.council.domain.bus.model.dto.BusArrivalDto;
import com.dku.council.domain.bus.model.dto.ResponseBusArrivalDto;
import com.dku.council.domain.bus.repository.BusArrivalRepository;
import com.dku.council.infra.bus.model.BusArrival;
import com.dku.council.infra.bus.service.OpenApiBusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusService {

    private final Clock clock;
    private final OpenApiBusService openApiBusService;
    private final BusArrivalRepository memoryRepository;

    public ResponseBusArrivalDto listBusArrival() {
        Instant now = Instant.now(clock);
        Instant capturedAt = Instant.now();
        List<BusArrivalDto> busArrivalEntranceList = new ArrayList<>();
        List<BusArrivalDto> busArrivalPlazaList = new ArrayList<>();

        List<BusStation> busStations = BusStation.getBusStations();

        for (BusStation busStation : busStations) {
            CachedBusArrivals cached = memoryRepository.getArrivals(busStation.name(), now)
                    .orElseGet(() -> {
                        List<BusArrival> arrivals = openApiBusService.retrieveBusArrival(busStation);
                        return memoryRepository.cacheArrivals(busStation.name(), arrivals, now);
                    });

            if (cached != null) {
                capturedAt = cached.getCapturedAt();
                Duration diff = Duration.between(cached.getCapturedAt(), now);

                if (busStation.equals(BusStation.DKU_GATE)) {
                    busArrivalEntranceList = cached.getArrivals().stream()
                            .map(arrival -> interpolation(arrival, diff))
                            .map(BusArrivalDto::new)
                            .collect(Collectors.toList());
                } else{
                    busArrivalPlazaList = cached.getArrivals().stream()
                            .map(arrival -> interpolation(arrival, diff))
                            .map(BusArrivalDto::new)
                            .collect(Collectors.toList());
                }
            }
        }
        return new ResponseBusArrivalDto(capturedAt, busArrivalEntranceList, busArrivalPlazaList);
    }

    private static BusArrival interpolation(BusArrival arrival, Duration diff) {
        Integer predictTime1 = arrival.getPredictTimeSec1();
        Integer predictTime2 = arrival.getPredictTimeSec2();

        if (predictTime1 != null) {
            predictTime1 -= (int) diff.getSeconds();
            predictTime1 = Math.max(predictTime1, 0);
        }

        if (predictTime2 != null) {
            predictTime2 -= (int) diff.getSeconds();
            predictTime2 = Math.max(predictTime2, 0);
        }

        return new BusArrival(arrival.getStatus(), arrival.getStationOrder(),
                arrival.getLocationNo1(), predictTime1, arrival.getPlateNo1(),
                arrival.getLocationNo2(), predictTime2, arrival.getPlateNo2(), arrival.getBusNo());
    }

    public void cacheBusArrival(BusStation station) {
        Instant now = Instant.now(clock);
        String stationName = station.name();

        List<BusArrival> arrivals = openApiBusService.retrieveBusArrival(station);
        memoryRepository.cacheArrivals(stationName, arrivals, now);
    }
}
