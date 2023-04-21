package com.example.diplomproject;

import java.util.Comparator;

public class SortListItems implements Comparator<ListElem> {
    @Override
    public int compare(ListElem o1, ListElem o2) {
        return o2.type - o1.type;
    }
}
