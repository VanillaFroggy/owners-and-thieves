package com.internship.model;

import java.util.List;

public record Thief(Apartment apartment, Backpack backpack) implements Runnable {
    @Override
    public void run() {
        List<Item> stolenItems = apartment.stealItems(backpack().maxWeight());
    }
}
