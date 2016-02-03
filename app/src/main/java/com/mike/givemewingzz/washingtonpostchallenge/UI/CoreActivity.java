package com.mike.givemewingzz.washingtonpostchallenge.UI;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mike.givemewingzz.washingtonpostchallenge.R;

import io.realm.Realm;

/*
* Base activity class that has some helper methods which is used in other activities.
* e.g. Refreshing of an Activity based on data receiving or received.
*
* */
public class CoreActivity extends AppCompatActivity {

    protected Realm realm;

    public SwipeRefreshLayout refresh;
    public FrameLayout container;

    public boolean toShowMenu;

    // Override all the setContentViews to use our layout.
    @Override
    public void setContentView(int layoutResID) {
        View.inflate(this, layoutResID, container);
    }

    @Override
    public void setContentView(View view) {
        container.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        container.addView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        container.addView(view, params);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        super.setContentView(R.layout.refreshable_container);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        container = (FrameLayout) findViewById(R.id.container);

        // Configure our refresher.
        refresh.setColorSchemeResources(R.color.teal1);
        refresh.setEnabled(false);
    }

    @Override
    protected void onDestroy() {

        if (realm != null) {
            realm.close();
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_core, menu);

        if (!isToShowMenu()) {
            return false;
        } else {
            return true;
        }
    }

    public void setMenuVisibility(final boolean toShow) {
        this.toShowMenu = toShow;
    }

    public boolean isToShowMenu() {
        return toShowMenu;
    }

    /*
     * Refresh helpers
     */
    public boolean isRefreshing() {
        return refresh.isRefreshing();
    }

    // Done in a callback to UI thread so we can getUserSelfInfo this in onCreate if we wish, or in non ui threads.
    public void setRefreshing(final boolean refreshing) {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(refreshing);
            }
        });
    }

    /*Default Snackbar for notifying user with some information*/

    public void displaySimpleConfirmSnackBar(View container, String msg) {
        // TODO: There is no design yet for error display.  Update this when that is available.
        Snackbar.make(container, msg, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.orange_11))
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

}
