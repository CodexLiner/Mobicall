package com.mobicall.call.sort;

import com.mobicall.call.models.contacts;

import java.util.Comparator;

public class SortByName implements Comparator<contacts> {
    @Override
    public int compare(contacts o1, contacts o2) {
        return o2.contact_name.compareTo(o1.getContact_name());
    }
}
