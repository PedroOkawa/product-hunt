package com.okawa.pedro.producthunt.util.manager;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.okawa.pedro.producthunt.ui.main.MainActivity;
import com.okawa.pedro.producthunt.ui.post.PostDetailsActivity;

/**
 * Created by pokawa on 19/02/16.
 */
public class CallManager {

    public void main(AppCompatActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static final String BUNDLE_POST_DETAILS_ID = "BUNDLE_POST_DETAILS_ID";
    private static final String TRANSITION_POST_VIEW = "TRANSITION_POST_VIEW";
    public void postDetails(AppCompatActivity activity, long postId, View view) {
        Intent intent = new Intent(activity, PostDetailsActivity.class);
        intent.putExtra(BUNDLE_POST_DETAILS_ID, postId);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, TRANSITION_POST_VIEW);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
