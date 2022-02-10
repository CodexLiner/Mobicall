package com.mobicall.call.models;

public class loginModel {
    boolean status;
    int isVerify;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(int isVerify) {
        this.isVerify = isVerify;
    }

    public loginModel(boolean status, int isVerify) {
        this.status = status;
        this.isVerify = isVerify;
    }
}
