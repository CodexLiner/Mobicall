package com.mobicall.call.models;

public class CompanyModel {
//    "id": 7,
//            "name": "bhagwan",
//            "phone": "7894561230",
//            "company": "test",
//            "email": "bhagwansawant9@gmail.com",
//            "created_at": "2022-01-01T01:53:25.000000Z",
//            "updated_at": "2022-02-06T10:28:29.000000Z"
    String id , name , phone , company , email , created_at,updated_at;

    public CompanyModel(String id, String name, String phone, String company, String email, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.company = company;
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
