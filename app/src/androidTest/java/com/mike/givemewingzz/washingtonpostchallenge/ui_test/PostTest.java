package com.mike.givemewingzz.washingtonpostchallenge.ui_test;

import android.app.Instrumentation;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;

import com.mike.givemewingzz.washingtonpostchallenge.R;
import com.mike.givemewingzz.washingtonpostchallenge.UI.PostsInfoActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Test cases that indicate the working of few UI elements including the activity.
 */
public class PostTest extends ActivityInstrumentationTestCase2<PostsInfoActivity> {

    PostsInfoActivity postsInfoActivity;
    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public PostTest() {
        super(PostsInfoActivity.class);
    }

    /**
     * Creates an {@link ActivityInstrumentationTestCase2}.
     *
     * @param activityClass The activity to test. This must be a class in the instrumentation
     *                      targetPackage specified in the AndroidManifest.xml
     */
    public PostTest(Class<PostsInfoActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        postsInfoActivity = getActivity();
        ButterKnife.bind(this, postsInfoActivity);

    }

    public void testLandingPageNotNull() {
        assertNotNull("Posts Activity is null", postsInfoActivity);
    }

    public void testViews() {
        assertNotNull("Recycler View is null", recyclerView);
    }

    public void testToolbarMenuItems() {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(PostsInfoActivity.class.getName(), null, false);

        postsInfoActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.performClick();
            }
        });

        postsInfoActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.performClick();
            }
        });

        PostsInfoActivity postsInfoActivity = (PostsInfoActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 2000);
        assertNotNull(postsInfoActivity);
        postsInfoActivity.finish();

    }

}
