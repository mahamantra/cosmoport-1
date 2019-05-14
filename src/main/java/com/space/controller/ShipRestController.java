package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class ShipRestController {

    public static final Logger logger = LoggerFactory.getLogger(ShipRestController.class);

    @Autowired
    private ShipService service;

    @GetMapping(value = "/ships")
    public List<Ship> getAllShips(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "planet", required = false) String planet,
                                  @RequestParam(value = "shipType", required = false) ShipType shipType,
                                  @RequestParam(value = "after", required = false) Long after,
                                  @RequestParam(value = "before", required = false) Long before,
                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                  @RequestParam(value = "minRating", required = false) Double minRating,
                                  @RequestParam(value = "maxRating", required = false) Double maxRating,
                                  @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {


        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return service.gelAllShips(Specification.where(service.filterByName(name)
                .and(service.filterByPlanet(planet)))
                .and(service.filterByShipType(shipType))
                .and(service.filterByDate(after, before))
                .and(service.filterByUsage(isUsed))
                .and(service.filterBySpeed(minSpeed, maxSpeed))
                .and(service.filterByCrewSize(minCrewSize, maxCrewSize))
                .and(service.filterByRating(minRating, maxRating))
                , pageable).getContent();
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public Integer getCount(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "planet", required = false) String planet,
                            @RequestParam(value = "shipType", required = false) ShipType shipType,
                            @RequestParam(value = "after", required = false) Long after,
                            @RequestParam(value = "before", required = false) Long before,
                            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                            @RequestParam(value = "minRating", required = false) Double minRating,
                            @RequestParam(value = "maxRating", required = false) Double maxRating) {

        return service.gelAllShips(Specification.where(service.filterByName(name)
                        .and(service.filterByPlanet(planet)))
                        .and(service.filterByShipType(shipType))
//                .and(service.filterByDate(after, before))
                        .and(service.filterByUsage(isUsed))
                        .and(service.filterBySpeed(minSpeed, maxSpeed))
                        .and(service.filterByCrewSize(minCrewSize, maxCrewSize))
                        .and(service.filterByRating(minRating, maxRating))).size();
    }

    @PostMapping(value = "/ships")
    public ResponseEntity addShip(@RequestBody Ship requestShip) {
        if (service.isValidForAdd(requestShip)) {
            Ship newShip = service.createShip(requestShip);
            return ResponseEntity.ok(newShip);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/ships/{id}")
    public ResponseEntity getShip(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0 || !(id instanceof Long))
            return ResponseEntity.badRequest().build();

        if (service.isShipExist(id))
            return ResponseEntity.ok(service.getShip(id));

        else return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/ships/{id}")
    public ResponseEntity editShip(@PathVariable(value = "id") Long id, @RequestBody Ship ship) {
        if (id == null || id <= 0 || !(id instanceof Long))
            return ResponseEntity.badRequest().build();

        if (!service.isShipExist(id))
            return ResponseEntity.notFound().build();

        if (service.isValidForEdit(ship))
            return ResponseEntity.ok(service.editShip(id, ship));

        else return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/ships/{id}")
    public ResponseEntity deleteShip(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0 || !(id instanceof Long))
            return ResponseEntity.badRequest().build();

        if (service.isShipExist(id)) {
            service.deleteById(id);
            return ResponseEntity.ok().build();

        } else return ResponseEntity.notFound().build();
    }
}
