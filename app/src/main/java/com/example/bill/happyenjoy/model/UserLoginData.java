package com.example.bill.happyenjoy.model;

import org.litepal.crud.DataSupport;

/**
 * Created by TT on 2017/10/4.
 */

public class UserLoginData extends DataSupport{

    private String phoneNumber;

    @Override
    public String toString() {
        return "UserLoginData{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
