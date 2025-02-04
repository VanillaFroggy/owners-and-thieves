package com.internship.model;

public record Thief(Apartment apartment, Backpack backpack) implements Runnable {
    @Override
    public void run() {
        apartment.stealItems(backpack().maxWeight());
    }
}
