package com.internship;

import com.internship.model.*;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Apartment apartment = new Apartment();

        List<Item> items1 = List.of(
                new Item(2, BigDecimal.valueOf(10)),
                new Item(3, BigDecimal.valueOf(20)),
                new Item(1, BigDecimal.valueOf(5))
        );
        List<Item> items2 = List.of(
                new Item(5, BigDecimal.valueOf(50)),
                new Item(2, BigDecimal.valueOf(15))
        );

        Thread owner1 = new Thread(new Owner(apartment, items1), "Owner1");
        Thread owner2 = new Thread(new Owner(apartment, items2), "Owner2");

        Thread thief1 = new Thread(new Thief(apartment, new Backpack(5)), "Thief1");
        Thread thief2 = new Thread(new Thief(apartment, new Backpack(6)), "Thief2");

        owner1.start();
        owner2.start();

        try {
            owner1.join();
            owner2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thief1.start();
        thief2.start();
    }
}
