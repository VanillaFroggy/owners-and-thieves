package com.internship.model;

import java.util.List;

public record Owner(Apartment apartment, List<Item> items) implements Runnable {
    @Override
    public void run() {
        items.forEach(item -> {
            apartment.addItem(item);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
