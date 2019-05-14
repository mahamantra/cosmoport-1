package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ShipService {


    Page<Ship> gelAllShips(Specification<Ship> specification, Pageable sortedByName);

    List<Ship> gelAllShips(Specification<Ship> specification);

    Ship createShip(Ship requestShip);

    Ship getShip(Long id);

    Ship editShip(Long id, Ship ship);

    void deleteById(Long id);

    boolean isValidForAdd(Ship ship);

    boolean isValidForEdit(Ship ship);

    boolean isShipExist(Long id);

    Specification<Ship> filterByPlanet(String planet);

    Specification<Ship> filterByName(String name);

    Specification<Ship> filterByShipType(ShipType shipType);

    Specification<Ship> filterByDate(Long after, Long before);

    Specification<Ship> filterByUsage(Boolean isUsed);

    Specification<Ship> filterBySpeed(Double min, Double max);

    Specification<Ship> filterByCrewSize(Integer min, Integer max);

    Specification<Ship> filterByRating(Double min, Double max);
}
