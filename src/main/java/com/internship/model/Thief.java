package com.internship.model;

public record Thief(Apartment apartment, Backpack backpack) implements Runnable {
    @Override
    public void run() {
        while (true) {
            if (apartment.stealItems(backpack().maxWeight()).isEmpty()) {
                break;
            }
        }
    }
}
