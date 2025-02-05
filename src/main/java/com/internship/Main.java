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
        List<Owner> owners = generateOwners(apartment, OWNERS_COUNT);
        List<Item> generatedItems = owners.stream()
                .flatMap(owner -> owner.items().stream())
                .toList();
        List<Thread> threads = new ArrayList<>(
                owners.stream()
                        .map(Thread::new)
                        .toList()
        );
        List<Thief> thieves = generateThieves(apartment, THIEVES_COUNT);
        threads.addAll(
                thieves.stream()
                        .map(Thread::new)
                        .toList()
        );
        Collections.shuffle(threads);
        threads.forEach(thread -> {
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        List<Item> resultedItems = new ArrayList<>(apartment.getItems());
        resultedItems.addAll(
                thieves.stream()
                        .flatMap(thief ->
                                thief.backpack()
                                        .items()
                                        .stream()
                        )
                        .toList()
        );
        System.out.println(
                generatedItems.size() == resultedItems.size()
                        && resultedItems.containsAll(generatedItems)
        );
    }

    private static List<Owner> generateOwners(Apartment apartment, int count) {
        List<Queue<Item>> itemLists = generateItems(count);
        List<Owner> owners = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            owners.add(new Owner(apartment, itemLists.get(i)));
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

    private static List<Thief> generateThieves(Apartment apartment, int count) {
        List<Thief> thieves = new ArrayList<>(count);
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            thieves.add(new Thief(
                    apartment,
                    new Backpack(random.nextInt(1, MAX_BACKPACK_WEIGHT), new ArrayList<>())
            ));
        }
        return thieves;
    }
}
