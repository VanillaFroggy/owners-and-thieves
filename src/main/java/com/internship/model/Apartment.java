package com.internship.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Apartment {
    private static final String NO_ITEMS_TO_STEAL_PHRASE = "Here is nothing to steal for ";
    private final List<Item> items = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition canComeIn = lock.newCondition();
    private int ownersInside = 0;
    private boolean thiefInside = false;

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        lock.lock();
        try {
            while (thiefInside) {
                canComeIn.await();
            }
            ownersInside++;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        items.add(item);
        System.out.println(Thread.currentThread().getName() + " added " + item);

        lock.lock();
        try {
            ownersInside--;
            if (ownersInside == 0) {
                canComeIn.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public List<Item> stealItems(int maxWeight) {
        lock.lock();
        try {
            while (ownersInside > 0 || thiefInside) {
                canComeIn.await();
            }
            thiefInside = true;
            if (items.isEmpty()) {
                thiefInside = false;
                canComeIn.signalAll();
                System.out.println(NO_ITEMS_TO_STEAL_PHRASE + Thread.currentThread().getName());
                return Collections.emptyList();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
        items.sort(Comparator.comparing(item ->
                (item.value()
                        .divide(
                                BigDecimal.valueOf(item.weight()),
                                RoundingMode.FLOOR
                        )
                ).negate()
        ));
        List<Item> stolen = new ArrayList<>();
        int currentWeight = 0;
        for (Item item : items) {
            if (currentWeight + item.weight() <= maxWeight) {
                stolen.add(item);
                currentWeight += item.weight();
            }
        }
        items.removeAll(stolen);
        System.out.println(
                stolen.isEmpty()
                        ? NO_ITEMS_TO_STEAL_PHRASE + Thread.currentThread().getName()
                        : Thread.currentThread().getName() + " stole " + stolen
        );
        lock.lock();
        try {
            thiefInside = false;
            canComeIn.signalAll();
        } finally {
            lock.unlock();
        }
        return stolen;
    }
}
