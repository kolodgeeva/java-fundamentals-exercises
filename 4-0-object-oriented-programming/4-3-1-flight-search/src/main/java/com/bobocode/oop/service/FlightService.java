package com.bobocode.oop.service;

import com.bobocode.oop.data.FlightDao;
import com.bobocode.oop.data.FlightDaoImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link FlightService} provides an API that allows to manage flight numbers
 * <p>
 * todo: 1. Using {@link FlightDaoImpl} implement method {@link FlightService#registerFlight(String)}
 * todo: 2. Using {@link FlightDaoImpl} implement method {@link FlightService#searchFlights(String)}
 */
public class FlightService {

    FlightDao flightDao;

    public FlightService(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    /**
     * Adds a new flight number
     *
     * @param flightNumber a flight number to add
     * @return {@code true} if a flight number was added, {@code false} otherwise
     */


    public boolean registerFlight(String flightNumber) {
        Objects.requireNonNull(flightNumber);
        return flightDao.register(flightNumber);
    }

    /**
     * Returns all flight numbers that contains a provided key.
     *
     * @param query a search query
     * @return a list of found flight numbers
     */
    public List<String> searchFlights(String query) {
        Objects.requireNonNull(query);
        return flightDao.findAll().stream()
            .filter(flight -> flight.contains(query))
            .collect(Collectors.toList());
    }
}
