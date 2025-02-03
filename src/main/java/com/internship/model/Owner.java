package com.internship.model;

import java.util.List;

public record Owner(Apartment apartment, List<Item> items) implements Runnable {
    @Override
    public void run() {
    }
}
