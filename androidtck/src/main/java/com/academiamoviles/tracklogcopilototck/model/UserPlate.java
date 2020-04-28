package com.academiamoviles.tracklogcopilototck.model;

/**
 * Created by veraj on 3/08/2018.
 */

public class UserPlate {

    Users users;
    Plates plates;

    public UserPlate(Users users, Plates plates) {
        this.users = users;
        this.plates = plates;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Plates getPlates() {
        return plates;
    }

    public void setPlates(Plates plates) {
        this.plates = plates;
    }
}
