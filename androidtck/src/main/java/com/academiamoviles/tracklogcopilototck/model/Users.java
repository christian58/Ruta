package com.academiamoviles.tracklogcopilototck.model;

/**
 * Created by veraj on 2/08/2018.
 */

public class Users {
    Integer idUsers;
    String firstname;
    String lastname;
    String phone;
    String username;

    public Users(String firstname, String lastname, String phone, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.username = username;
    }

    public Integer getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(Integer idUsers) {
        this.idUsers = idUsers;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
