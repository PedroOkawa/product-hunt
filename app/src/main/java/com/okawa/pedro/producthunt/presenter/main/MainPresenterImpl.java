package com.okawa.pedro.producthunt.presenter.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.model.event.ApiEvent;
import com.okawa.pedro.producthunt.model.event.ConnectionEvent;
import com.okawa.pedro.producthunt.model.event.PostSelectEvent;
import com.okawa.pedro.producthunt.model.list.PostContent;
import com.okawa.pedro.producthunt.util.builder.ParametersBuilder;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.adapter.post.AdapterPost;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.listener.OnRecyclerViewListener;
import com.okawa.pedro.producthunt.util.listener.OnTouchListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;
import com.okawa.pedro.producthunt.util.manager.HeaderGridLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import greendao.Category;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainPresenterImpl implements MainPresenter, OnTouchListener, DatePickerDialog.OnDateSetListener {

    private MainView mainView;
    private ApiManager apiManager;
    private ConfigHelper configHelper;
    private ParametersBuilder parametersBuilder;
    private DatabaseRepository databaseRepository;

    private ActivityMainBinding binding;
    private Context context;

    private AdapterPost adapterPost;
    private HeaderGridLayoutManager headerGridLayoutManager;
    private OnPostsRecyclerViewListener onPostsRecyclerViewListener;

    private OnMenuItemSelectedListener onMenuItemSelectedListener;

    private DatePickerDialog datePickerDialog;
    private Date currentDate;
    private boolean isFilterOpen;

    public MainPresenterImpl(MainView mainView,
                             ApiManager apiManager,
                             ConfigHelper configHelper,
                             ParametersBuilder parametersBuilder,
                             DatabaseRepository databaseRepository) {
        this.mainView = mainView;
        this.apiManager = apiManager;
        this.configHelper = configHelper;
        this.parametersBuilder = parametersBuilder;
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

        adapterPost = new AdapterPost(new ArrayList<PostContent>());
        headerGridLayoutManager = new HeaderGridLayoutManager(context, configHelper, adapterPost);
        onPostsRecyclerViewListener = new OnPostsRecyclerViewListener(headerGridLayoutManager);

        binding.rvActivityMainPosts.setAdapter(adapterPost);
        binding.rvActivityMainPosts.setLayoutManager(headerGridLayoutManager);
        binding.rvActivityMainPosts.addOnScrollListener(onPostsRecyclerViewListener);
        binding.srlActivityMainPosts.setOnRefreshListener(new OnPostsRefreshListener());

        /* NAVIGATION VIEW */

        onMenuItemSelectedListener = new OnMenuItemSelectedListener();
        binding.navigationView.navigationView.setNavigationItemSelectedListener(onMenuItemSelectedListener);

        /* TOOLBAR */

        mainView.initializeToolbar(databaseRepository.getCurrentCategoryName());

        /* FILTER LAYOUT */

        binding.setTouchListener(this);

        /* SPINNER FILTER */

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.filter_activity_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spActivityMainSort.setAdapter(adapter);
        binding.spActivityMainSort.setOnItemSelectedListener(new OnOrderItemSelectedListener());

        /* SORT CONDITIONS */

        databaseRepository.setOrderBy(DatabaseRepository.ORDER_BY_ID);
        databaseRepository.setWhereType(DatabaseRepository.WHERE_ALL);

        /* PARAMETERS BUILDER */

        parametersBuilder.init().setPagination();

        /* DATE PICKER */

        isFilterOpen = false;

        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.getTime();

        datePickerDialog = new DatePickerDialog(context,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                context.getString(R.string.date_picker_cancel),
                new OnCancelDateListener());

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                context.getString(R.string.date_picker_select),
                new OnCancelDateListener());

        /* REQUEST INITIAL DATA */

        requestCategoryData();
    }

    @Override
    public void openDatePicker() {
        datePickerDialog.show();
    }

    @Override
    public boolean isFilterOpen() {
        return isFilterOpen;
    }

    @Override
    public void closeFilter() {
        resetFilters();
    }

    @Override
    public void dispose() {

        /* UNREGISTER ON EVENT BUS */

        EventBus.getDefault().unregister(this);
    }

    private void requestCategoryData() {
        /* INITIALIZE CATEGORIES SUB MENU */

        if(databaseRepository.checkCategoriesLoaded()) {
            initializeNavigationMenu();
        } else {
            apiManager.requestCategories();
        }
    }

    private void initializeNavigationMenu() {
        SubMenu categories = binding
                .navigationView
                .navigationView
                .getMenu().addSubMenu(R.string.navigation_menu_categories);

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

        /* GENERATE PARAMETERS */

        if(isFilterOpen) {

            Map<String, String> parameters = parametersBuilder
                    .init()
                    .setDay(configHelper.convertDateToString(currentDate))
                    .setPagination()
                    .generateParameters();

            apiManager.requestPostsByCategory(parameters);
        } else {

            Map<String, String> parameters = parametersBuilder
                    .init()
                    .setOlder(databaseRepository.getLastPostId())
                    .setCategory(databaseRepository.getCurrentCategorySlug())
                    .setPagination()
                    .generateParameters();

            apiManager.requestPosts(parameters);
        }
    }

    private void resetData() {
        adapterPost.reset();
        onPostsRecyclerViewListener.reset();
        requestData();
    }

    private void resetFilters() {
        isFilterOpen = false;

        binding.spActivityMainSort.setSelection(DatabaseRepository.ORDER_BY_VOTE);
        binding.llActivityMainFilterOptions.animate().translationY(0);
        binding.toolbarShadow.animate().translationY(0);
        binding.filterHolder.setVisibility(View.GONE);

        databaseRepository.resetLastPostSession();
        databaseRepository.setOrderBy(DatabaseRepository.ORDER_BY_ID);
        databaseRepository.setWhereType(DatabaseRepository.WHERE_ALL);

        parametersBuilder.init().setPagination();

        resetData();
    }

    @Subscribe
    public void onEvent(PostSelectEvent event) {
        mainView.openPostDetails(event.getPostId(), event.getView());
    }

    @Subscribe
    public void onEvent(ConnectionEvent event) {
        requestData();
    }

    @Subscribe
    public void onEvent(ApiEvent apiEvent) {
        if(apiEvent.isError()) {
            mainView.onError(apiEvent.getMessage());
        } else {
            if(apiEvent.getType() == ApiEvent.PROCESS_POSTS_ID) {
                adapterPost.addDataSet(databaseRepository.selectPostsByCategory(currentDate, context, adapterPost.getItemCount()));
                mainView.onComplete(adapterPost.getItemCount() == 0);
            } else if(apiEvent.getType() == ApiEvent.PROCESS_CATEGORIES_ID) {
                initializeNavigationMenu();
            }
        }
    }

    @Override
    public void onViewTouched(View view) {
        if(view.getId() == R.id.tvActivityMainFilterBody) {
            resetFilters();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        currentDate = calendar.getTime();
    }

    protected class OnCancelDateListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                isFilterOpen = true;
                DatePicker datePicker = datePickerDialog.getDatePicker();

                onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                binding.llActivityMainFilterOptions.animate().translationY(binding.llActivityMainFilterOptions.getMeasuredHeight());
                binding.toolbarShadow.animate().translationY(binding.llActivityMainFilterOptions.getMeasuredHeight());
                binding.filterHolder.setVisibility(View.VISIBLE);
                binding.tvActivityMainFilterBody.setText(configHelper.getDateString(context, currentDate));

                databaseRepository.setOrderBy(DatabaseRepository.ORDER_BY_VOTE);
                databaseRepository.setWhereType(DatabaseRepository.WHERE_DATE);

                resetData();
            }
        }
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

    protected class OnOrderItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(isFilterOpen) {
                databaseRepository.setWhereType(DatabaseRepository.WHERE_DATE);

                switch (position) {
                    case DatabaseRepository.ORDER_BY_VOTE:
                        databaseRepository.setOrderBy(DatabaseRepository.ORDER_BY_VOTE);
                        break;
                    case DatabaseRepository.ORDER_BY_TITLE:
                        databaseRepository.setOrderBy(DatabaseRepository.ORDER_BY_TITLE);
                        break;
                    case DatabaseRepository.ORDER_BY_USER:
                        databaseRepository.setOrderBy(DatabaseRepository.ORDER_BY_USER);
                        break;
                }

                parametersBuilder
                        .init()
                        .setDay(configHelper.convertDateToString(currentDate))
                        .setPagination();

                resetData();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    protected class OnMenuItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        private MenuItem previousItem;

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            String title = item.getTitle().toString();
            databaseRepository.setCurrentCategory(title);
            databaseRepository.resetLastPostSession();
            mainView.setToolbarName(title);
            binding.dlActivityMain.closeDrawers();

            if(previousItem != null) {
                previousItem.setChecked(false);
            }

            item.setChecked(true);
            previousItem = item;

            resetData();
            return false;
        }

        public void setPreviousItem(MenuItem menuItem) {
            this.previousItem = menuItem;
        }
    }
}
