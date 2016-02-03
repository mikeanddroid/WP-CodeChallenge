package com.mike.givemewingzz.washingtonpostchallenge.utils;

import android.content.Context;

import io.realm.RealmConfiguration;

public class DBHelper {

    public static final long SCHEMA_VERSION = 1;
    public static final String REALM_NAME = "com.mike.givemewingz.washingtonpostdb";

    public static RealmConfiguration getRealmConfig(Context context) {
        return new RealmConfiguration.Builder(context)
                .name(REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)
                .build();
    }

}
