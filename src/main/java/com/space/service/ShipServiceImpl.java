package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Page<Ship> gelAllShips(Pageable sortedByName) {
        return shipRepository.findAll(sortedByName);
    }

    @Override
    public List<Ship> gelAllShips() {
        return shipRepository.findAll();
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
}
