package com.mike.givemewingzz.washingtonpostchallenge.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mike.givemewingzz.washingtonpostchallenge.R;
import com.mike.givemewingzz.washingtonpostchallenge.adapters.PostsAdapter;
import com.mike.givemewingzz.washingtonpostchallenge.data.model.PostsData;
import com.mike.givemewingzz.washingtonpostchallenge.service.APIHelper;
import com.mike.givemewingzz.washingtonpostchallenge.utils.AppConstants;
import com.mike.givemewingzz.washingtonpostchallenge.utils.EventBusSingleton;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PostsInfoActivity extends CoreActivity implements PostsAdapter.EventListener {

    private static final String TAG = PostsInfoActivity.class.getSimpleName();
    private Toolbar toolbar;

    @Bind(R.id.list)
    public RecyclerView recyclerView;

    public PostsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Bind(R.id.landingPage)
    public ViewGroup viewGroup;

    /* Used to notify the databse when information has been changed */
    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            adapter.swapData(getPostsFromDb());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.postsinfotransition));
        }

        setContentView(R.layout.posts_info_layout);
        ButterKnife.bind(this); // Used to bind views

        initToolbar();

        /* We want to set the menu visibility to Visible because this activity requires a toolbar */
        setMenuVisibility(true);

        // Set initial Refreshing state to true
        setRefreshing(true);

        /* Make a call to get the posts from server */
        APIHelper.getPosts(PostsInfoActivity.this);
        initView();

        /* Used when the data set is changed and this notifies the database to update the information */
        realm.addChangeListener(realmChangeListener);

        Animation animation;
        animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        viewGroup.startAnimation(animation);

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBusSingleton.unregister(this);

        Animation animation;
        animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        viewGroup.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBusSingleton.register(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*Initially load the view with information available in the database till the new data is fetched.
    * The reason being, a user doesn't have to wait for the data to be previewed in the Screen if there's a slow connection
    * or some server error. At this point, a user will mostly have some data presented.
    */
    private void initView() {
        RealmResults<PostsData> realmResults = getPostsFromDb();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostsAdapter(this, realmResults, true);
        adapter.setEventListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_core);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int menuId = item.getItemId();
                RealmResults<PostsData> realmResults = getPostsFromDb();
                switch (menuId) {

                    case R.id.sort_by_name:
                        sortByName(realmResults);
                        break;
                    case R.id.sort_by_date:
                        sortByDateDesc(realmResults);
                        break;

                }

                return true;
            }
        });

    }

    /* Sort the Titles with ascending order */
    private void sortByName(RealmResults<PostsData> realmResults) {
        realmResults.sort("postTitle", RealmResults.SORT_ORDER_ASCENDING);
        adapter.swapData(realmResults);
    }

    /* Sort the Dates with descending order */
    private void sortByDateDesc(RealmResults<PostsData> realmResults) {
        realmResults.sort("postDate", RealmResults.SORT_ORDER_DESCENDING);
        adapter.swapData(realmResults);
    }

    /* Notify the UI that the result is OK and ready to be used somehow*/
    @Subscribe
    public void onPostSuccess(APIHelper.PostsInfoSuccess postInfo) {
        setRefreshing(false);
    }

    public RealmResults<PostsData> getPostsFromDb() {
        RealmResults<PostsData> realmResults = realm.where(PostsData.class).findAll();
        return realmResults;
    }

    /* Present user with some error message when there's an issue while retrieving data */
    @Subscribe
    public void onPostFailure(APIHelper.PostsInfoFailure error) {
        setRefreshing(false);
        displaySimpleConfirmSnackBar(recyclerView, error.getErrorMessage());
    }

    /* Go to the next activity with some values to be presented.
    * Since the information is not very large, it can just be sent using Intent-Extras or we can just pass and Item ID
    * to the next screen and that ID wil be used to fetch remaining items
    */
    @Override
    public void onItemClick(View view, PostsData postsData) {

        ActivityOptionsCompat optionsCompat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName("xyz");
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, view.getTransitionName());
        }

        Intent intent = new Intent(this, PostDetails.class);
        intent.putExtra(AppConstants.CONTENT_KEY, postsData.getPostContent());
        intent.putExtra(AppConstants.DATE_KEY, postsData.getPostDate());
        intent.putExtra(AppConstants.TITLE_KEY, postsData.getPostTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, optionsCompat.toBundle());
        }

    }

}
