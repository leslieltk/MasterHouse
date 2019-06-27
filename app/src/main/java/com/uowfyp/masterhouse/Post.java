package com.uowfyp.masterhouse;

public class Post {
    String name;
    String key;
    String title;
    String uid;
    String postDate;
    String description;
    String location;
    String salary;
    String type;
    String username;

    public Post(){
    }

    public Post( String key, String title, String description, String uid, String location, String salary, String type, String username) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.uid = uid;
        this.location = location;
        this.salary = salary;
        this.type = type;
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
