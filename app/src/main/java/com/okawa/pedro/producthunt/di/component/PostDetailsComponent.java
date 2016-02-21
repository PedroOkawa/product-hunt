package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.di.module.PostDetailsModule;
import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.post.PostDetailsPresenter;
import com.okawa.pedro.producthunt.ui.post.PostDetailsActivity;
import com.okawa.pedro.producthunt.ui.post.PostDetailsView;

import dagger.Component;

/**
 * Created by pokawa on 21/02/16.
 */
@Activity
@Component( dependencies = AppComponent.class, modules = PostDetailsModule.class )
public interface PostDetailsComponent {

    void inject(PostDetailsActivity postDetailsActivity);

    /* POST DETAILS */

    PostDetailsView providesPostDetailsView();
    PostDetailsPresenter providesPostDetailsPresenter();

}
