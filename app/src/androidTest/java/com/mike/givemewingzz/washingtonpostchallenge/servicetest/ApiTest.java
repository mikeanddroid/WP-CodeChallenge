package com.mike.givemewingzz.washingtonpostchallenge.servicetest;

import android.test.AndroidTestCase;

import com.mike.givemewingzz.washingtonpostchallenge.data.model.PostsDataWrapper;
import com.mike.givemewingzz.washingtonpostchallenge.service.BaseClient;
import com.mike.givemewingzz.washingtonpostchallenge.service.RetrofitInterface;

public class ApiTest extends AndroidTestCase {

    /* A simple test to check the incoming data from the server. */
    public void testRepairStatusSync() {
        //Get our API
        RetrofitInterface retrofitInterface = BaseClient.getRetrofitInterface();

        //Get a single status item
        PostsDataWrapper data = retrofitInterface.getPosts();
        //Check some of the data
        assertNotNull(data);
        assertNotNull(data.getPostsList());

        /*Can also add a case where there's a problem or failure*/

    }

}
