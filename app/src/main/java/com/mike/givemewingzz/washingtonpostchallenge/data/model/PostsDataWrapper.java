package com.mike.givemewingzz.washingtonpostchallenge.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PostsDataWrapper extends RealmObject {

    @SerializedName("posts")
    private RealmList<PostsData> postsList;

    public RealmList<PostsData> getPostsList() {
        return postsList;
    }

    public void setPostsList(RealmList<PostsData> postsList) {
        this.postsList = postsList;
    }
}
