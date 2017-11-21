package com.example.bill.happyenjoy.model;

/**
 * Created by 雨晴 on 2017/11/8.
 */

public class RegisterResponseJson extends BaseJson {
    private RegisterResponseData data;

    public RegisterResponseData getData() {
        return data;
    }

    public void setData(RegisterResponseData data) {
        this.data = data;
    }
}
