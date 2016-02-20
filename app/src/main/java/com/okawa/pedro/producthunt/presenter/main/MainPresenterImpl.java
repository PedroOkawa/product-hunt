package com.okawa.pedro.producthunt.presenter.main;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.SubMenu;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.CategoryRepository;
import com.okawa.pedro.producthunt.database.PostRepository;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.adapter.AdapterPost;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import java.util.ArrayList;
import java.util.Date;

import greendao.Category;
import greendao.Post;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainPresenterImpl implements MainPresenter, ApiListener {

    private MainView mainView;
    private ApiManager apiManager;
    private ConfigHelper configHelper;
    private CategoryRepository categoryRepository;
    private PostRepository postRepository;

    private ActivityMainBinding binding;

    private AdapterPost adapterPost;
    private GridLayoutManager gridLayoutManager;

    public MainPresenterImpl(MainView mainView,
                             ApiManager apiManager,
                             ConfigHelper configHelper,
                             CategoryRepository categoryRepository,
                             PostRepository postRepository) {
        this.mainView = mainView;
        this.apiManager = apiManager;
        this.configHelper = configHelper;
        this.categoryRepository = categoryRepository;
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

        requestInitialData();
    }

    private void requestInitialData() {
        apiManager.requestPostsByDate(this, new Date());

        /* INITIALIZE CATEGORIES SUB MENU */

        if(categoryRepository.checkCategoriesLoaded()) {
            initializeCategoriesMenu();
        } else {
            apiManager.requestCategories(this);
        }
    }

    @Override
    public void onDataLoaded(int process) {
        if(process == ApiManager.PROCESS_POSTS_ID) {
            adapterPost.addDataSet(postRepository.selectPostByDate(new Date()));
        } else if(process == ApiManager.PROCESS_CATEGORIES_ID) {
            initializeCategoriesMenu();
        }
    }

    @Override
    public void onError(String error) {
        mainView.onError(error);
    }

    private void initializeCategoriesMenu() {
        SubMenu categories = binding
                .navigationView
                .navigationView
                .getMenu().addSubMenu(R.string.navigation_menu_categories);

        for(Category category : categoryRepository.selectCategories()) {
            categories.add(category.getName());
        }
    }
}
