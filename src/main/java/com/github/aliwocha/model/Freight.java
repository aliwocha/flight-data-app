package com.github.aliwocha.model;

import java.util.Objects;

public abstract class Freight {

    private Integer id;
    private Double weight;
    private String weightUnit;
    private Integer pieces;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(final Double weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(final String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(final Integer pieces) {
        this.pieces = pieces;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Freight freight = (Freight) o;
        return Objects.equals(id, freight.id) && Objects.equals(weight, freight.weight)
                && Objects.equals(weightUnit, freight.weightUnit) && Objects.equals(pieces, freight.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, weightUnit, pieces);
    }
}
