package com.mobicall.call.sort;

import com.mobicall.call.models.contacts;

import java.util.Comparator;

public class SortByDate implements Comparator<contacts> {

    @Override
    public int compare(contacts o1, contacts o2) {
        return o1.updated_at.compareTo(o2.updated_at);
    }
}
