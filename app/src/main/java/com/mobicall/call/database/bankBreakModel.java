package com.mobicall.call.database;

public class bankBreakModel {
    int COLUMN_ID;
    String STATUS;

    public bankBreakModel(int COLUMN_ID, String STATUS) {
        this.COLUMN_ID = COLUMN_ID;
        this.STATUS = STATUS;
    }

    public int getCOLUMN_ID() {
        return COLUMN_ID;
    }

    public void setCOLUMN_ID(int COLUMN_ID) {
        this.COLUMN_ID = COLUMN_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    @Override
    public String toString() {
        return "bankDataModel{" +
                "COLUMN_ID=" + COLUMN_ID +
                ", STATUS='" + STATUS + '\'' +
                '}';
    }
}
