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
import com.okawa.pedro.producthunt.model.event.PostSelectEvent;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.adapter.AdapterPost;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.listener.OnRecyclerViewListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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

    private OnMenuItemSelectedListener onMenuItemSelectedListener;

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

        /* REGISTER ON EVENT BUS */

        EventBus.getDefault().register(this);

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

        onMenuItemSelectedListener = new OnMenuItemSelectedListener();
        binding.navigationView.navigationView.setNavigationItemSelectedListener(onMenuItemSelectedListener);

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
    public void dispose() {

        /* UNREGISTER ON EVENT BUS */

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDataLoaded(int process) {
        if(process == ApiManager.PROCESS_POSTS_ID) {
            adapterPost.addDataSet(databaseRepository.selectPostsByCategoryPaged(adapterPost.getItemCount()));
            mainView.onComplete();
        } else if(process == ApiManager.PROCESS_CATEGORIES_ID) {
            initializeNavigationMenu();
        }
    }

    @Override
    public void onError(String error) {
        mainView.onError(error);
    }

    private void requestCategoryData() {
        /* INITIALIZE CATEGORIES SUB MENU */

        if(databaseRepository.checkCategoriesLoaded()) {
            initializeNavigationMenu();
        } else {
            apiManager.requestCategories(this);
        }
    }

    private void initializeNavigationMenu() {
        SubMenu categories = binding
                .navigationView
                .navigationView
                .getMenu().addSubMenu(R.string.navigation_menu_categories);

        binding.navigationView
                .navigationView
                .getMenu().add(R.string.navigation_menu_collections)
                .setCheckable(true);

        for(Category category : databaseRepository.selectCategories()) {
            MenuItem menuItem = categories.add(category.getName()).setCheckable(true);

            if(category.getId().equals(Category.CATEGORY_TECH_ID)) {
                onMenuItemSelectedListener.setPreviousItem(menuItem);
                menuItem.setChecked(true);
            }
        }
    }

    private void requestData() {
        mainView.onRequest();
        apiManager.requestPostsByDaysAgo(this);
    }

    private void resetData() {
        adapterPost.reset();
        onPostsRecyclerViewListener.reset();
        databaseRepository.resetDaysAgo();
        requestData();
    }

    @Subscribe
    public void onEvent(PostSelectEvent event) {
        mainView.openPostDetails(event.getPostId(), event.getView());
    }

    protected class OnPostsRecyclerViewListener extends OnRecyclerViewListener {

        public OnPostsRecyclerViewListener(GridLayoutManager gridLayoutManager) {
            super(gridLayoutManager);
        }

        @Override
        public void onVisibleThreshold() {
            requestData();
        }
    }

    protected class OnPostsRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            resetData();
        }
    }

    protected class OnMenuItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        private MenuItem previousItem;

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            databaseRepository.setCurrentCategory(item.getTitle().toString());
            binding.dlActivityMain.closeDrawers();
            resetData();

            item.setChecked(true);
            if(previousItem != null) {
                previousItem.setChecked(false);
            }
            previousItem = item;
            return false;
        }

        public void setPreviousItem(MenuItem menuItem) {
            this.previousItem = menuItem;
        }
    }
}
