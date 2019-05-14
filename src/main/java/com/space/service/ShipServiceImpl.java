package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ShipServiceImpl implements ShipService {

    public static final Logger logger = LoggerFactory.getLogger(ShipServiceImpl.class);

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Page<Ship> gelAllShips(Specification<Ship> specification, Pageable sortedByName) {
        return shipRepository.findAll(specification, sortedByName);
    }

    @Override
    public List<Ship> gelAllShips(Specification<Ship> specification) {
        return shipRepository.findAll(specification);
    }

    @Override
    public boolean isValidForAdd(Ship ship) {
        return !(ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null
                || isTextWrong(ship.getName())
                || isTextWrong(ship.getPlanet())
                || isDateWrong(ship.getProdDate())
                || isSpeedWrong(ship.getSpeed())
                || isCrewSizeWrong(ship.getCrewSize()));
    }

    @Override
    public boolean isValidForEdit(Ship ship) {
        boolean flag = true;

        if (ship.getName() != null)
            flag = flag && !isTextWrong(ship.getName());

        if (ship.getPlanet() != null)
            flag = flag && !isTextWrong(ship.getPlanet());

        if (ship.getProdDate() != null)
            flag = flag && !isDateWrong(ship.getProdDate());

        if (ship.getSpeed() != null)
            flag = flag && !isSpeedWrong(ship.getSpeed());

        if (ship.getCrewSize() != null)
            flag = flag && !isCrewSizeWrong(ship.getCrewSize());

        return flag;
    }

    private boolean isCrewSizeWrong(Integer crewSize) {
        if (crewSize < 1 || crewSize > 9999)
            return true;
        return false;
    }

    private boolean isSpeedWrong(Double speed) {
        if (speed < 0.01 || speed > 0.99)
            return true;
        return false;
    }

    private boolean isDateWrong(Date date) {
        Date min = new Date(26192246400000L);
        Date max = new Date(33134659200000L);
        if (date.getTime() < 0 || date.before(min) || date.after(max))
            return true;
        return false;
    }

    private boolean isTextWrong(String text) {
        if (Objects.equals(text, "") || text.length() > 50)
            return true;
        return false;
    }


    /**
     * Calculates ship rating and put ship in DB.
     *
     * @param ship ship entity from request
     * @return ship with rating and id
     */

    @Override
    public Ship createShip(Ship ship) {
        if (ship.getUsed() == null)
            ship.setUsed(false);

        Double raiting = calculateRating(ship);
        ship.setRating(raiting);

        return shipRepository.save(ship);
    }

    private Double calculateRating(Ship ship) {
        //get year
        //TODO rewrite in new Date API
        Calendar cal = Calendar.getInstance();
        cal.setTime(ship.getProdDate());
        int year = cal.get(Calendar.YEAR);

        //calculate rating
        BigDecimal raiting = new BigDecimal((80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1)) / (3019 - year + 1));
        //round rating to 2 decimal places
        raiting = raiting.setScale(2, RoundingMode.HALF_UP);
        return raiting.doubleValue();
    }

    @Override
    public Ship editShip(Long id, Ship ship) {
        Ship editedShip = shipRepository.findById(id).get();

        if (ship.getName() != null)
            editedShip.setName(ship.getName());

        if (ship.getPlanet() != null)
            editedShip.setPlanet(ship.getPlanet());

        if (ship.getShipType() != null)
            editedShip.setShipType(ship.getShipType());

        if (ship.getProdDate() != null)
            editedShip.setProdDate(ship.getProdDate());

        if (ship.getSpeed() != null)
            editedShip.setSpeed(ship.getSpeed());

        if (ship.getUsed() != null)
            editedShip.setUsed(ship.getUsed());

        if (ship.getCrewSize() != null)
            editedShip.setCrewSize(ship.getCrewSize());

        Double rating = calculateRating(editedShip);
        editedShip.setRating(rating);

        return shipRepository.save(editedShip);
    }

    /**
     * Checks if ship exists in DB by Ship unique ID.
     *
     * @param id ship id
     * @return true if Ship exists and false if not.
     */

    @Override
    public boolean isShipExist(Long id) {
        if (shipRepository.existsById(id))
            return true;

        else return false;
    }

    @Override
    public Ship getShip(Long id) {
        return shipRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {

        shipRepository.deleteById(id);
    }

    @Override
    public Specification<Ship> filterByName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("name"), "%" + name + "%");
    }

    @Override
    public Specification<Ship> filterByPlanet(String planet) {
        return (root, query, cb) -> planet == null ? null : cb.like(root.get("planet"), "%" + planet + "%");
    }

    @Override
    public Specification<Ship> filterByShipType(ShipType shipType) {
        return (root, query, cb) -> shipType == null ? null : cb.equal(root.get("shipType"), shipType);
    }

    @Override
    public Specification<Ship> filterByDate(Long after, Long before) {
        return (root, query, cb) -> {
            if (after == null && before == null)
                return null;
            if (after == null) {
                Date before1 = new Date(before);
                return cb.lessThanOrEqualTo(root.get("prodDate"), before1);
            }
            if (before == null) {
                Date after1 = new Date(after);
                return cb.greaterThanOrEqualTo(root.get("prodDate"), after1);
            }
            Date before1 = new Date(before);
            Date after1 = new Date(after);
            return cb.between(root.get("prodDate"), after1, before1);
        };
    }

    @Override
    public Specification<Ship> filterByUsage(Boolean isUsed) {
        return (root, query, cb) -> {
            if (isUsed == null)
                return null;
            if (isUsed)
                return cb.isTrue(root.get("isUsed"));
            else return cb.isFalse(root.get("isUsed"));
        };
    }

    @Override
    public Specification<Ship> filterBySpeed(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null)
                return null;
            if (min == null)
                return cb.lessThanOrEqualTo(root.get("speed"), max);
            if (max == null)
                return cb.greaterThanOrEqualTo(root.get("speed"), min);

            return cb.between(root.get("speed"), min, max);
        };
    }

    @Override
    public Specification<Ship> filterByCrewSize(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null)
                return null;
            if (min == null)
                return cb.lessThanOrEqualTo(root.get("crewSize"), max);
            if (max == null)
                return cb.greaterThanOrEqualTo(root.get("crewSize"), min);

            return cb.between(root.get("crewSize"), min, max);
        };
    }

    @Override
    public Specification<Ship> filterByRating(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null)
                return null;
            if (min == null)
                return cb.lessThanOrEqualTo(root.get("rating"), max);
            if (max == null)
                return cb.greaterThanOrEqualTo(root.get("rating"), min);

            return cb.between(root.get("rating"), min, max);
        };
    }
}
