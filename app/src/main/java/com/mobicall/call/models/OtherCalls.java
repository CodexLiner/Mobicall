package com.mobicall.call.models;

public class OtherCalls {
    String name , number , callType ;

    public OtherCalls(String name, String number, String callType) {
        this.name = name;
        this.number = number;
        this.callType = callType;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getCallType() {
        return callType;
    }
}
