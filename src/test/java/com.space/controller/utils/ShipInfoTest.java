package com.space.controller.utils;

import com.space.model.ShipType;

import java.util.Date;
import java.util.Objects;

public class ShipInfoTest {
    public Long id;
    public String name;
    public String planet;
    public ShipType shipType;
    public Long prodDate;
    public Boolean isUsed;
    public Double speed;
    public Integer crewSize;
    public Double rating;

    public ShipInfoTest() {
    }

    public ShipInfoTest(Long id, String name, String planet, ShipType shipType, Long prodDate, Boolean isUsed, Double speed, Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipInfoTest shipInfoTest = (ShipInfoTest) o;
        return Objects.equals(id, shipInfoTest.id) &&
                Objects.equals(name, shipInfoTest.name) &&
                Objects.equals(planet, shipInfoTest.planet) &&
                shipType == shipInfoTest.shipType &&
                Objects.equals(new Date(prodDate).getYear(), new Date(prodDate).getYear()) &&
                Objects.equals(isUsed, shipInfoTest.isUsed) &&
                Objects.equals(speed, shipInfoTest.speed) &&
                Objects.equals(crewSize, shipInfoTest.crewSize) &&
                Objects.equals(rating, shipInfoTest.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }
}