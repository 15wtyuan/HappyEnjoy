package com.example.bill.happyenjoy.model;

import org.litepal.crud.DataSupport;

/**
 * Created by 雨晴 on 2017/11/21.
 */

public class RegisterResponseData extends DataSupport{
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }
}
