package com.thoughtworks.rslist.domain;

public class UserView {
    private String user_name;
    private int user_age;
    private String user_gender;
    private String user_email;
    private String user_phone;

    public UserView(User user) {
        this.user_name = user.getUserName();
        this.user_age = user.getAge();
        this.user_gender = user.getGender();
        this.user_email = user.getEmail();
        this.user_phone = user.getPhone();
    }

    public UserView(String user_name, int user_age, String user_gender, String user_email, String user_phone) {
        this.user_name = user_name;
        this.user_age = user_age;
        this.user_gender = user_gender;
        this.user_email = user_email;
        this.user_phone = user_phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
