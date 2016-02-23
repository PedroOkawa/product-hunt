package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.post.PostDetailsPresenter;
import com.okawa.pedro.producthunt.presenter.post.PostDetailsPresenterImpl;
import com.okawa.pedro.producthunt.ui.post.PostDetailsView;
import com.okawa.pedro.producthunt.util.builder.ParametersBuilder;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 21/02/16.
 */
@Module
public class PostDetailsModule {

    private PostDetailsView postDetailsView;

    public PostDetailsModule(PostDetailsView postDetailsView) {
        this.postDetailsView = postDetailsView;
    }

    @Activity
    @Provides
    public PostDetailsView providesPostDetailsView() {
        return postDetailsView;
    }

    @Activity
    @Provides
    public PostDetailsPresenter providesPostDetailsPresenter(PostDetailsView postDetailsView,
                                                             ApiManager apiManager,
                                                             ParametersBuilder parametersBuilder,
                                                             DatabaseRepository databaseRepository) {
        return new PostDetailsPresenterImpl(postDetailsView, apiManager, parametersBuilder, databaseRepository);
    }

}
