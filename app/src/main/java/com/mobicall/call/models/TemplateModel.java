package com.mobicall.call.models;

public class TemplateModel {
//    "id": 1,
//            "admin_id": "7",
//            "message": "test",
//            "created_at": "2022-02-07T03:06:03.000000Z",
//            "updated_at": "2022-02-07T03:06:03.000000Z"
    String id , admin_id , message , created_at , updated_at ,name;

    public TemplateModel(String id, String admin_id, String message, String created_at, String updated_at, String name) {
        this.id = id;
        this.admin_id = admin_id;
        this.message = message;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
