package com.mobicall.call.models;

public class totalCount {
//    	"connected": 3,
//                "not_connected": 0,
//                "interested": 3,
//                "not_interested": 0,
//                "total": 3
    String connected, not_connected,interested , not_interested ,total , null_call_status;

    public totalCount(String connected, String not_connected, String interested, String not_interested, String total, String null_call_status) {
        this.connected = connected;
        this.not_connected = not_connected;
        this.interested = interested;
        this.not_interested = not_interested;
        this.total = total;
        this.null_call_status = null_call_status;
    }

    public String getConnected() {
        return connected;
    }

    public void setConnected(String connected) {
        this.connected = connected;
    }

    public String getNot_connected() {
        return not_connected;
    }

    public void setNot_connected(String not_connected) {
        this.not_connected = not_connected;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getNot_interested() {
        return not_interested;
    }

    public void setNot_interested(String not_interested) {
        this.not_interested = not_interested;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNull_call_status() {
        return null_call_status;
    }

    public void setNull_call_status(String null_call_status) {
        this.null_call_status = null_call_status;
    }
}
