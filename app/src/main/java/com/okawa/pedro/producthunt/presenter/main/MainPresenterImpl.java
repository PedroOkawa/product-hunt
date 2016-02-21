package com.okawa.pedro.producthunt.presenter.main;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.view.SubMenu;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.adapter.AdapterPost;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.listener.OnRecyclerViewListener;
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
    private DatabaseRepository databaseRepository;

    private ActivityMainBinding binding;
    private Context context;

    private AdapterPost adapterPost;
    private GridLayoutManager gridLayoutManager;
    private OnPostsRecyclerViewListener onPostsRecyclerViewListener;

    public MainPresenterImpl(MainView mainView,
                             ApiManager apiManager,
                             ConfigHelper configHelper,
                             DatabaseRepository databaseRepository) {
        this.mainView = mainView;
        this.apiManager = apiManager;
        this.configHelper = configHelper;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void initialize(ActivityMainBinding binding) {

        /* STORES BINDING */

        this.binding = binding;

        /* CONTEXT */

        context = binding.getRoot().getContext();

        /* RECYCLER VIEW */

        adapterPost = new AdapterPost(new ArrayList<Post>());
        gridLayoutManager = new GridLayoutManager(context, configHelper.defineSpanCount(context));
        onPostsRecyclerViewListener = new OnPostsRecyclerViewListener(gridLayoutManager);

        binding.rvActivityMainPosts.setAdapter(adapterPost);
        binding.rvActivityMainPosts.setLayoutManager(gridLayoutManager);
        binding.rvActivityMainPosts.addOnScrollListener(onPostsRecyclerViewListener);
        binding.srlActivityMainPosts.setOnRefreshListener(new OnPostsRefreshListener());

        /* NAVIGATION VIEW */

        binding.navigationView.navigationView.setNavigationItemSelectedListener(new OnMenuItemSelectedListener());

        /* TOOLBAR */

        mainView.initializeToolbar();

        /* REQUEST INITIAL DATA */

        requestCategoryData();
    }

    @Override
    public void updateGridLayoutSpan() {
        gridLayoutManager.setSpanCount(configHelper.defineSpanCount(context));
    }

    @Override
    public void onDataLoaded(int process) {
        if(process == ApiManager.PROCESS_POSTS_ID) {
            adapterPost.addDataSet(databaseRepository.selectPostsByCategoryPaged(adapterPost.getItemCount()));
            mainView.onComplete();
        } else if(process == ApiManager.PROCESS_CATEGORIES_ID) {
            initializeCategoriesMenu();
        }
    }

    @Override
    public void onError(String error) {
        mainView.onError(error);
    }

    private void requestCategoryData() {
        /* INITIALIZE CATEGORIES SUB MENU */

        if(databaseRepository.checkCategoriesLoaded()) {
            initializeCategoriesMenu();
        } else {
            apiManager.requestCategories(this);
        }
    }

    private void resetDataList() {
        adapterPost.reset();
        onPostsRecyclerViewListener.reset();
        databaseRepository.resetDaysAgo();
        requestDataDaysAgo();
    }

    private void requestDataByDay(Date date) {
        mainView.onRequest();
        apiManager.requestPostsByDate(this, date);
    }

    private void requestDataDaysAgo() {
        mainView.onRequest();
        apiManager.requestPostsByDaysAgo(this);
    }

    private void initializeCategoriesMenu() {
        SubMenu categories = binding
                .navigationView
                .navigationView
                .getMenu().addSubMenu(R.string.navigation_menu_categories);

        for(Category category : databaseRepository.selectCategories()) {
            categories.add(category.getName());
        }
    }

    protected class OnPostsRecyclerViewListener extends OnRecyclerViewListener {

        public OnPostsRecyclerViewListener(GridLayoutManager gridLayoutManager) {
            super(gridLayoutManager);
        }

        @Override
        public void onVisibleThreshold() {
            requestDataDaysAgo();
        }
    }

    protected class OnPostsRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            resetDataList();
        }
    }

    protected class OnMenuItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            databaseRepository.setCurrentCategory(item.getTitle().toString());
            binding.dlActivityMain.closeDrawers();
            resetDataList();
            return false;
        }
    }
}
