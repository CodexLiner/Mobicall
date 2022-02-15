package com.mobicall.call.database;

public class userDatabaseModel {
    public static final String TABLE_NAME = "ARASKO";

    public static final String COLUMN_ID = "id";
    public static final String ADMIN_ID = "admin_id";
    public static final String COLUMN_NOTE = "mobile";
    public static final String AUTHKEY = "token";
    public static final String DATE = "date";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String COMPANYNAME = "companyName";
    public static final String COMPANYMAIL = "companyEmail";
    public static final String FROMDATE = "from_date";
    public static final String TODATE = "to_date";

    private int id = 1;
    private String mobile;
    private String auth;
    private String email;
    private String name;
    private String date;
    private String cName;
    private String cMail;
    private String from;
    private String to;
    public userDatabaseModel(int id, String mobile, String auth, String email, String name, String date, String cName, String cMail, String from, String to) {
        this.id = id;
        this.mobile = mobile;
        this.auth = auth;
        this.email = email;
        this.name = name;
        this.date = date;
        this.cName = cName;
        this.cMail = cMail;
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public userDatabaseModel(int id, String mobile, String auth, String email, String name, String date, String cName, String cMail) {
        this.id = id;
        this.mobile = mobile;
        this.auth = auth;
        this.email = email;
        this.name = name;
        this.date = date;
        this.cName = cName;
        this.cMail = cMail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcMail() {
        return cMail;
    }

    public void setcMail(String cMail) {
        this.cMail = cMail;
    }

    public static String DbName = "ARASKO_USER";
    public static final String
            CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER,"
                    + ADMIN_ID + " INTEGER,"
                    + COLUMN_NOTE + " TEXT,"
                    + DATE + " TEXT DEFAULT "+"'01/01/1900',"
                    + EMAIL + " TEXT DEFAULT "+"'mail@arasko.com',"
                    + NAME + " TEXT DEFAULT "+"'User',"
                    + COMPANYNAME + " TEXT DEFAULT "+"'COMPANY',"
                    + FROMDATE + " TEXT DEFAULT "+"'12:00',"
                    + TODATE + " TEXT DEFAULT "+"'12:00',"
                    + COMPANYMAIL + " TEXT DEFAULT "+"'company@company.com',"
                    + AUTHKEY + " TEXT DEFAULT Guest"
                    + ")";

    @Override
    public String toString() {
        return "userDatabaseModel{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", auth='" + auth + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", cName='" + cName + '\'' +
                ", cMail='" + cMail + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
