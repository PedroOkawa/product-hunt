package com.okawa.pedro.producthunt.presenter.loading;

import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.ui.loading.LoadingView;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

/**
 * Created by pokawa on 19/02/16.
 */
public class LoadingPresenterImpl implements LoadingPresenter, ApiListener {

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
    public void validateToken() {
        loadingView.onRequest();

        if(databaseRepository.containsSession()) {
            loadingView.onComplete();
        } else {
            apiManager.validateSession(this);
        }
    }

    @Override
    public void onDataLoaded(int process) {
        loadingView.onComplete();
    }

    @Override
    public void onError(String error) {
        loadingView.onError(error);
    }
}
