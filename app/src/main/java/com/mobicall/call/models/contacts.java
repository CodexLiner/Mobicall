package com.mobicall.call.models;

public class contacts {
//    {
//            "id": 1,
//            "user_id": "2",
//            "contact_name": "test 1",
//            "phone": "7894561232",
//            "email": "test1@gmail.com",
//            "interested": "1",
//            "description": "test",
//            "call_status": null,
//            "talktime": null,
//            "call_recording": null,
//            "created_at": "2022-02-06T12:44:11.000000Z",
//            "updated_at": "2022-02-06T12:45:20.000000Z"
//    },
    String id ,user_id,contact_name , phone , email , interested , description , call_status , talktime , call_recording , created_at , updated_at;

    public contacts(String id, String user_id, String contact_name, String phone, String email, String interested, String description, String call_status, String talktime, String call_recording, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.contact_name = contact_name;
        this.phone = phone;
        this.email = email;
        this.interested = interested;
        this.description = description;
        this.call_status = call_status;
        this.talktime = talktime;
        this.call_recording = call_recording;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCall_status() {
        return call_status;
    }

    public void setCall_status(String call_status) {
        this.call_status = call_status;
    }

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }

    public String getCall_recording() {
        return call_recording;
    }

    public void setCall_recording(String call_recording) {
        this.call_recording = call_recording;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
