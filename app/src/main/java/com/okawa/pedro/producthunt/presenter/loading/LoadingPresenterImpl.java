package com.okawa.pedro.producthunt.presenter.loading;

import com.okawa.pedro.producthunt.database.SessionRepository;
import com.okawa.pedro.producthunt.ui.loading.LoadingView;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

/**
 * Created by pokawa on 19/02/16.
 */
public class LoadingPresenterImpl implements LoadingPresenter, ApiListener {

    private LoadingView loadingView;
    private ApiManager apiManager;
    private SessionRepository sessionRepository;

    public LoadingPresenterImpl(LoadingView loadingView,
                                ApiManager apiManager,
                                SessionRepository sessionRepository) {
        this.loadingView = loadingView;
        this.apiManager = apiManager;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void validateToken() {
        loadingView.onLoadData();

        if(sessionRepository.containsSession()) {
            loadingView.onDataLoaded();
        } else {
            apiManager.validateSession(this);
        }
    }

    @Override
    public void onDataLoaded() {
        loadingView.onDataLoaded();
    }

    @Override
    public void onError(String error) {
        loadingView.onError(error);
    }
}
