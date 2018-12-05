package com.artc.utils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

public class CollectionTest {

    public static void main(String[] args) {

        Collection<Integer> c = new LinkedHashSet<>();

        for (int i = 0; i < 1000000; i++) {
            c.add(i);
        }

        long start = System.nanoTime();
        Object[] objects = c.toArray();
        Object object = objects[0];
        long end = System.nanoTime();
        System.out.println("end = " + (end - start) / 1000000000.0);

        start = System.nanoTime();
        Optional<Integer> first = c.stream().findFirst();
        end = System.nanoTime();
        System.out.println("end = " + (end - start) / 1000000000.0);

    }

}
