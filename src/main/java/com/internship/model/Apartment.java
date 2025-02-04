package com.internship.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Apartment {
    private static final String NO_ITEMS_TO_STEAL_PHRASE = "Here is nothing to steal for ";
    private final List<Item> items = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition canSteal = lock.newCondition();
    private boolean ownerInside = false;

    public void addItem(Item item) {
        lock.lock();
        try {
            ownerInside = true;
            items.add(item);
            System.out.println(Thread.currentThread().getName() + " added " + item);
        } finally {
            ownerInside = false;
            canSteal.signalAll();
            lock.unlock();
        }
    }

    public List<Item> stealItems(int maxWeight) {
        lock.lock();
        try {
            while (ownerInside) {
                canSteal.await();
            }
            if (items.isEmpty()) {
                System.out.println(NO_ITEMS_TO_STEAL_PHRASE + Thread.currentThread().getName());
                return Collections.emptyList();
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
            for (Iterator<Item> iterator = items.iterator(); iterator.hasNext();) {
                Item item = iterator.next();
                if (currentWeight + item.weight() <= maxWeight) {
                    stolen.add(item);
                    currentWeight += item.weight();
                    iterator.remove();
                }
            }
            System.out.println(
                    stolen.isEmpty()
                            ? NO_ITEMS_TO_STEAL_PHRASE + Thread.currentThread().getName()
                            : Thread.currentThread().getName() + " stole " + stolen
            );
            return stolen;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
