package com.okawa.pedro.producthunt.presenter.loading;

import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.model.event.ApiEvent;
import com.okawa.pedro.producthunt.ui.loading.LoadingView;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by pokawa on 19/02/16.
 */
public class LoadingPresenterImpl implements LoadingPresenter {

    private LoadingView loadingView;
    private ApiManager apiManager;
    private DatabaseRepository databaseRepository;

    public LoadingPresenterImpl(LoadingView loadingView,
                                ApiManager apiManager,
                                DatabaseRepository databaseRepository) {
        this.loadingView = loadingView;
        this.apiManager = apiManager;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void initialize() {

        /* REGISTER ON EVENT BUS */

        EventBus.getDefault().register(this);

    }

    @Override
    public void validateToken() {
        loadingView.onRequest();

        if(databaseRepository.containsSession()) {
            loadingView.onComplete();
        } else {
            apiManager.validateSession();
        }
    }

    @Override
    public void dispose() {

        /* UNREGISTER ON EVENT BUS */

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ApiEvent apiEvent) {
        if(apiEvent.getType() == ApiEvent.PROCESS_SESSION_ID) {
            if (apiEvent.isError()) {
                loadingView.onError(apiEvent.getMessage());
            } else {
                loadingView.onComplete();
            }
        }
    }
}
