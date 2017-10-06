package com.example.bill.happyenjoy.model;

/**
 * Created by TT on 2017/10/5.
 */

public class BaseJson {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int error_code) {
        this.code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String messgage) {
        this.message = messgage;
    }
}
