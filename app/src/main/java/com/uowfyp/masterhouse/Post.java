package com.uowfyp.masterhouse;

public class Post {
    String name;
    String key;
    String title;
    String uid;
    String postDate;
    String description;

    public Post(){
    }

    public Post( String key, String title, String description, String uid) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.uid = uid;
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
}
