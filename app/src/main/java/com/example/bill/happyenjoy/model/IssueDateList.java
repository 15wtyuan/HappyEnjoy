package com.example.bill.happyenjoy.model;

import java.util.List;

/**
 * Created by TT on 2017/11/5.
 */

public class IssueDateList {

    List<UserData> user;
    List<IssueDate> data;
    int i;

    public List<UserData> getUser() {
        return user;
    }

    public void setUser(List<UserData> user) {
        this.user = user;
    }

    public List<IssueDate> getData() {
        return data;
    }

    public void setData(List<IssueDate> data) {
        this.data = data;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
