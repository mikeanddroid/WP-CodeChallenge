package com.mike.givemewingzz.washingtonpostchallenge.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PostsData extends RealmObject {

    @SerializedName("id")
    private int postID;

    @SerializedName("title")
    private String postTitle;

    @SerializedName("date")
    private String postDate;

    @SerializedName("content")
    private String postContent;

    /* Requires an empty constructor */
    public PostsData() {
    }

    public PostsData(PostsData postsData) {
        this.postID = postsData.getPostID();
        this.postTitle = postsData.getPostTitle();
        this.postContent = postsData.getPostContent();
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}
