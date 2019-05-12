package com.space.controller;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class ShipController {

    @Autowired
    private ShipService shipService;
    int page;

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getShips() {
        return shipService.findAll();
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public Integer getCount() {
        return shipService.findAll().size();
    }

    @GetMapping(value = "/ships/{id}")
    public ResponseEntity getShip(@PathVariable(value = "id") Long id){
        return shipService.getShip(id);
    }

    @DeleteMapping(value = "/ships/{id}")
    public ResponseEntity deleteShip(@PathVariable(value = "id") Long id) {
        return shipService.deleteById(id);
    }
}
