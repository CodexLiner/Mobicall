package com.mobicall.call.sort;

import com.mobicall.call.models.contacts;

import java.util.Comparator;

public class SortByStatus implements Comparator<contacts> {

    @Override
    public int compare(contacts c1, contacts c2) {
        return c1.getCall_status().compareTo(c2.getCall_status());
    }
}
