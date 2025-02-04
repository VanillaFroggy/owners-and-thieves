package com.internship.model;

import java.util.Queue;

public record Owner(Apartment apartment, Queue<Item> items) implements Runnable {
    @Override
    public void run() {
        while (!items.isEmpty()) {
            apartment.addItem(items.poll());
        }
    }
}
