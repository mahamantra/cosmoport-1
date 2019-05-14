package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class ShipRestController {

    public static final Logger logger = LoggerFactory.getLogger(ShipRestController.class);

    @Autowired
    private ShipService shipService;

    @GetMapping(value = "/ships")
    public List<Ship> getAllShips(@RequestParam(value = "order", required = false,defaultValue = "ID") ShipOrder order,
                                  @RequestParam(value = "pageNumber", required = false,defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

            Pageable sortedByName = (order == ShipOrder.ID) ? PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())) : PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()).descending());
        return shipService.gelAllShips(sortedByName).getContent();
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public Integer getCount(@RequestParam(value = "order", required = false,defaultValue = "ID") ShipOrder order,
                            @RequestParam(value = "pageNumber", required = false,defaultValue = "0") Integer pageNumber,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        return shipService.gelAllShips().size();
    }

    @PostMapping(value = "/ships")
    public ResponseEntity addShip(@RequestBody Ship requestShip) {
        if (shipService.isValidForAdd(requestShip)) {
            Ship newShip = shipService.createShip(requestShip);
            return ResponseEntity.ok(newShip);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/ships/{id}")
    public ResponseEntity getShip(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0 || !(id instanceof Long))
            return ResponseEntity.badRequest().build();

        if (shipService.isShipExist(id))
            return ResponseEntity.ok(shipService.getShip(id));

        else return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/ships/{id}")
    public ResponseEntity editShip(@PathVariable(value = "id") Long id, @RequestBody Ship ship) {
        if (id == null || id <= 0 || !(id instanceof Long))
            return ResponseEntity.badRequest().build();

        if (!shipService.isShipExist(id))
            return ResponseEntity.notFound().build();

        if (shipService.isValidForEdit(ship))
            return ResponseEntity.ok(shipService.editShip(id, ship));

        else return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/ships/{id}")
    public ResponseEntity deleteShip(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0 || !(id instanceof Long))
            return ResponseEntity.badRequest().build();

        if (shipService.isShipExist(id)) {
            shipService.deleteById(id);
            return ResponseEntity.ok().build();

        } else return ResponseEntity.notFound().build();
    }
}
