package com.okawa.pedro.producthunt.ui.post;

import android.os.Bundle;
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
    protected void doOnCreated(Bundle savedInstanceState) {
        binding = (ActivityPostDetailsBinding) getDataBinding();

        long postId = getIntent()
                .getLongExtra(CallManager.BUNDLE_POST_DETAILS_ID, Long.MIN_VALUE);

        postDetailsPresenter.initialize(binding, postId, getLayoutInflater());
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
    public void onError(String error) {
        binding.pbActivityPostDetails.hide();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
