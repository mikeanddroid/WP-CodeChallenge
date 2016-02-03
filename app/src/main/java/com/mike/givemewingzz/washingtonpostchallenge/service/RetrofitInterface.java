package com.mike.givemewingzz.washingtonpostchallenge.service;

import com.mike.givemewingzz.washingtonpostchallenge.data.model.PostsDataWrapper;

import retrofit.Callback;
import retrofit.http.GET;

/* Setting the interface for calling the desired endpoints */
public interface RetrofitInterface {

    @GET("/wp-srv/simulation/simulation_test.json")
    void getPosts(Callback<PostsDataWrapper> cb);

    /*Used in AndroidTestCase*/
    @GET("/wp-srv/simulation/simulation_test.json")
    PostsDataWrapper getPosts();

}
