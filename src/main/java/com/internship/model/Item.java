package com.internship.model;

import java.math.BigDecimal;

public record Item(int weight, BigDecimal value) {
    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
