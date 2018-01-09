package com.example.bill.happyenjoy.model;

/**
 * Created by TT on 2017/12/16.
 */

public class CommentariesData {

    private int id;
    private int user1_id;
    private int user2_id;
    private int issue_id;
    private String context;
    private String commentTime;
    private String user1_image;
    private String user2_image;
    private String user1_flowerName;
    private String user2_flowerName;

    public String getUser1_flowerName() {
        return user1_flowerName;
    }

    public void setUser1_flowerName(String user1_flowerName) {
        this.user1_flowerName = user1_flowerName;
    }

    public String getUser2_flowerName() {
        return user2_flowerName;
    }

    public void setUser2_flowerName(String user2_flowerName) {
        this.user2_flowerName = user2_flowerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(int user1_id) {
        this.user1_id = user1_id;
    }

    public int getUser2_id() {
        return user2_id;
    }

    public void setUser2_id(int user2_id) {
        this.user2_id = user2_id;
    }

    public int getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getUser1_image() {
        return user1_image;
    }

    public void setUser1_image(String user1_image) {
        this.user1_image = user1_image;
    }

    public String getUser2_image() {
        return user2_image;
    }

    public void setUser2_image(String user2_image) {
        this.user2_image = user2_image;
    }
}
