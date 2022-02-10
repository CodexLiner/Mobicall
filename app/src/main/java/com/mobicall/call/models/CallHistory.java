package com.mobicall.call.models;

public class CallHistory {
    String number , name ,date , time , type;

    public CallHistory(String number, String name, String date, String time, String type) {
        this.number = number;
        this.name = name;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
