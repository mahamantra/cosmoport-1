package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public List<Ship> findAll() {
        return shipRepository.findAll();
    }


    public ResponseEntity deleteById(Long id) {
        if(id == null || id <= 0 || !(id instanceof Long))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        if (!shipRepository.existsById(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        shipRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity getShip(Long id) {
        if(id == null || id <= 0 || !(id instanceof Long))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        if (!shipRepository.existsById(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        Ship ship = shipRepository.findById(id).get();
        return new ResponseEntity<Ship>(ship, HttpStatus.OK);
    }
}
