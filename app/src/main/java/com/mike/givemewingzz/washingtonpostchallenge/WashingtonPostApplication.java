package com.mike.givemewingzz.washingtonpostchallenge;

import android.app.Application;
import android.content.Context;

import com.mike.givemewingzz.washingtonpostchallenge.utils.DBHelper;
import com.mike.givemewingzz.washingtonpostchallenge.utils.EventBusSingleton;
import com.squareup.otto.Bus;

import io.realm.Realm;

public class WashingtonPostApplication extends Application {

    public static Context context;
    public static WashingtonPostApplication application;

    // Hold reference to event bus.
    Bus bus = EventBusSingleton.getBus();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = this.getApplicationContext();

        // Setup Realm for database interaction.
        Realm.setDefaultConfiguration(DBHelper.getRealmConfig(this));

    }

    /* Get an instance of the Application Context if needed. */
    public static WashingtonPostApplication getInstance() {
        return application;
    }

}
