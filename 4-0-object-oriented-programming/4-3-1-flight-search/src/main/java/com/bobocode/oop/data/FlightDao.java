package com.bobocode.oop.data;

import java.util.Set;

public interface FlightDao {

  boolean register(String flightNumber);

  Set<String> findAll();

}
