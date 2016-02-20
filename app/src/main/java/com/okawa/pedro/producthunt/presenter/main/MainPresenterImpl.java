package com.okawa.pedro.producthunt.presenter.main;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.okawa.pedro.producthunt.database.PostRepository;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.adapter.AdapterPost;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import java.util.ArrayList;
import java.util.Date;

import greendao.Post;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainPresenterImpl implements MainPresenter, ApiListener {

    private MainView mainView;
    private ApiManager apiManager;
    private ConfigHelper configHelper;
    private PostRepository postRepository;

    private ActivityMainBinding binding;

    private AdapterPost adapterPost;
    private GridLayoutManager gridLayoutManager;

    public MainPresenterImpl(MainView mainView,
                             ApiManager apiManager,
                             ConfigHelper configHelper,
                             PostRepository postRepository) {
        this.mainView = mainView;
        this.apiManager = apiManager;
        this.configHelper = configHelper;
        this.postRepository = postRepository;
    }

    @Override
    public void initialize(ActivityMainBinding binding) {

        /* STORES BINDING */

        this.binding = binding;

        /* CONTEXT */

        Context context = binding.getRoot().getContext();

        /* RECYCLER VIEW */

        adapterPost = new AdapterPost(new ArrayList<Post>());
        gridLayoutManager = new GridLayoutManager(context, configHelper.defineSpanCount(context));

        binding.rvActivityMainPosts.setAdapter(adapterPost);
        binding.rvActivityMainPosts.setLayoutManager(gridLayoutManager);

        /* REQUEST INITIAL DATA */

        requestTodayData();
    }

    private void requestTodayData() {
        apiManager.requestPostsByDay(this);
        apiManager.requestCategories(this);
    }

    @Override
    public void onDataLoaded(int process) {
        if(process == ApiManager.PROCESS_POSTS_ID) {
            adapterPost.addDataSet(postRepository.selectPostByDate(new Date()));
        } else if(process == ApiManager.PROCESS_CATEGORIES_ID) {
            Log.wtf("TEST", "CATEGORIES");
        }
    }

    @Override
    public void onError(String error) {
        mainView.onError(error);
    }
}
