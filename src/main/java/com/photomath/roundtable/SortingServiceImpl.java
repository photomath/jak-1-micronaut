package com.photomath.roundtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class SortingServiceImpl {

    static long sorting(long arraySize) {
        long start = System.currentTimeMillis();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= arraySize; i++) {
            list.add(UUID.randomUUID().toString());
        }
        Collections.sort(list);
        long end = System.currentTimeMillis();
        long timeSpent = end - start;
        return timeSpent;
    }
}
