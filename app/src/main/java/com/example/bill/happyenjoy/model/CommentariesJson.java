package com.example.bill.happyenjoy.model;

import java.util.List;

/**
 * Created by TT on 2017/12/17.
 */

public class CommentariesJson extends BaseJson {

    private List<CommentariesData> data;

    public List<CommentariesData> getData() {
        return data;
    }

    public void setData(List<CommentariesData> data) {
        this.data = data;
    }
}
