package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    private String userName;
    @NotNull
    private String gender;
    @NotNull
    @Min(18)
    @Max(100)
    private int age;
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "1\\w{10}")
    private String phone;
    private int voteNumber = 10;

    public User() {
    }

    public User(String userName, String gender, int age, String email, String phone) {
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", voteNumber=" + voteNumber +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    @JsonProperty("user_gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    @JsonProperty("user_age")
    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("user_email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    @JsonProperty("user_phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    @JsonProperty("user_vote_number")
    public void setVoteNumber(int voteNumber) {
        this.voteNumber = voteNumber;
    }
}
