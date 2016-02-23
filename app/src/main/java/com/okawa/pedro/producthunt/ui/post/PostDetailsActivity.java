package com.okawa.pedro.producthunt.ui.post;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityPostDetailsBinding;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerPostDetailsComponent;
import com.okawa.pedro.producthunt.di.module.PostDetailsModule;
import com.okawa.pedro.producthunt.presenter.post.PostDetailsPresenter;
import com.okawa.pedro.producthunt.ui.common.BaseActivity;
import com.okawa.pedro.producthunt.util.manager.CallManager;

import javax.inject.Inject;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostDetailsActivity extends BaseActivity implements PostDetailsView {

    private ActivityPostDetailsBinding binding;

    @Inject
    PostDetailsPresenter postDetailsPresenter;

    @Override
    protected int layoutToInflate() {
        return R.layout.activity_post_details;
    }

    @Override
    protected void initializeComponent(AppComponent appComponent) {
        DaggerPostDetailsComponent
                .builder()
                .appComponent(appComponent)
                .postDetailsModule(new PostDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        postDetailsPresenter.setActive(false);
    }

    @Override
    protected void doOnCreated(Bundle savedInstanceState) {
        binding = (ActivityPostDetailsBinding) getDataBinding();

        long postId = getIntent()
                .getLongExtra(CallManager.BUNDLE_POST_DETAILS_ID, Long.MIN_VALUE);

        postDetailsPresenter.initialize(binding, postId, getLayoutInflater());
    }

    @Override
    public void initializeToolbar(String title) {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void initializeStatusBar() {
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void changeStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }

    @Override
    public void onRequest() {
        binding.pbActivityPostDetails.show();
    }

    @Override
    public void onComplete() {
        binding.pbActivityPostDetails.hide();
    }

    @Override
    public void onErrorComments(String error) {
        binding.pbActivityPostDetails.hide();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorVotes(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
