package com.space.service;

import com.space.model.Ship;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShipService {
    
    
    ResponseEntity deleteById(Long id);

    ResponseEntity getShip(Long id);

    List<Ship> findAll();
}
