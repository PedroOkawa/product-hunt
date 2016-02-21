package com.okawa.pedro.producthunt.presenter.post;

import com.bumptech.glide.Glide;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.databinding.ActivityPostDetailsBinding;
import com.okawa.pedro.producthunt.ui.post.PostDetailsView;
import com.okawa.pedro.producthunt.util.helper.GlideCircleTransform;

import greendao.Post;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostDetailsPresenterImpl implements PostDetailsPresenter {

    private PostDetailsView postDetailsView;
    private DatabaseRepository databaseRepository;

    private ActivityPostDetailsBinding binding;

    public PostDetailsPresenterImpl(PostDetailsView postDetailsView,
                                    DatabaseRepository databaseRepository) {
        this.postDetailsView = postDetailsView;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void initialize(ActivityPostDetailsBinding binding, long postId) {

        /* STORES BINDING */

        this.binding = binding;

        /* INITIALIZES VIEWS */

        Post post = databaseRepository.selectPostById(postId);

        this.binding.viewPostDetails.setPost(post);

        /* POST PREVIEW */

        Glide.with(binding.getRoot().getContext())
                .load(post.getThumbnail().getImage())
                .asBitmap()
                .centerCrop()
                .into(binding.viewPostDetails.ivViewPostDetailsPreview);

        /* USER AVATAR */

        Glide.with(binding.getRoot().getContext())
                .load(post.getUser().getAvatar().getOriginal())
                .asBitmap()
                .centerCrop()
                .transform(new GlideCircleTransform(binding.getRoot().getContext()))
                .into(binding.viewPostDetails.ivViewPostDetailsUser);
    }
}
