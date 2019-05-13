package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipService {
    
    
    Page<Ship> gelAllShips(Pageable sortedByName);

    List<Ship> gelAllShips();

    Ship createShip(Ship requestShip);

    Ship getShip(Long id);

    Ship editShip(Long id, Ship ship);

    void deleteById(Long id);

    boolean isValidForAdd(Ship ship);

    boolean isValidForEdit(Ship ship);

    boolean isShipExist(Long id);
}
