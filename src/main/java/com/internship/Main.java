package com.internship;

import com.internship.model.*;

import java.math.BigDecimal;
import java.util.*;

public class Main {
    public static final int OWNERS_COUNT = 500;
    public static final int THIEVES_COUNT = 500;
    public static final int MAX_ITEM_VALUE = 100_000;
    public static final int MAX_ITEM_WEIGHT = 10;
    public static final int MAX_ITEMS_COUNT = 10;
    public static final int MAX_BACKPACK_WEIGHT = 15;

    public static void main(String[] args) {
        Apartment apartment = new Apartment();
        List<Thread> threads = generateOwners(apartment, OWNERS_COUNT);
        threads.addAll(generateThieves(apartment, THIEVES_COUNT));
        Collections.shuffle(threads);
        threads.forEach(Thread::start);
    }

    private static List<Thread> generateOwners(Apartment apartment, int count) {
        List<Queue<Item>> itemLists = generateItems(count);
        List<Thread> owners = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            owners.add(new Thread(
                    new Owner(apartment, itemLists.get(i)),
                    "Owner" + (i + 1))
            );
        }
        return owners;
    }

    private static List<Queue<Item>> generateItems(int count) {
        List<Queue<Item>> itemLists = new ArrayList<>(count);
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int itemsCount = random.nextInt(1, MAX_ITEMS_COUNT);
            itemLists.add(new LinkedList<>());
            for (int j = 0; j < itemsCount; j++) {
                itemLists.get(i).add(new Item(
                        random.nextInt(1, MAX_ITEM_WEIGHT),
                        BigDecimal.valueOf(random.nextInt(1, MAX_ITEM_VALUE))
                ));
            }
        }
        return itemLists;
    }

    private static List<Thread> generateThieves(Apartment apartment, int count) {
        List<Thread> thieves = new ArrayList<>(count);
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            thieves.add(new Thread(
                    new Thief(
                            apartment,
                            new Backpack(random.nextInt(1, MAX_BACKPACK_WEIGHT))
                    ),
                    "Thief" + (i + 1)
            ));
        }
        return thieves;
    }
}
