package com.mobicall.call.models;

public class user {
//    "user": {
//        "id": 2,
//                "name": "Carl Bond",
//                "email": "fycynuzyly@mailinator.com",
//                "phone": "8089749247",
//                "from_time": "12:00:00",
//                "to_time": "14:46:00",
//                "admin_id": "7",
//                "created_at": "2022-02-06T12:17:41.000000Z",
//                "updated_at": "2022-02-06T12:18:10.000000Z"
//    }
    String id ,name , email , phone , from_time , to_time , admin_id , created_at , updated_at ;

    public user(String id, String name, String email, String phone, String from_time, String to_time, String admin_id, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.from_time = from_time;
        this.to_time = to_time;
        this.admin_id = admin_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
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

    @Override
    public String toString() {
        return "user{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", from_time='" + from_time + '\'' +
                ", to_time='" + to_time + '\'' +
                ", admin_id='" + admin_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
