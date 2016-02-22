package com.okawa.pedro.producthunt.presenter.post;

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.databinding.ActivityPostDetailsBinding;
import com.okawa.pedro.producthunt.databinding.AdapterCommentBinding;
import com.okawa.pedro.producthunt.ui.post.PostDetailsView;
import com.okawa.pedro.producthunt.util.helper.GlideCircleTransform;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import greendao.Comment;
import greendao.Post;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostDetailsPresenterImpl implements PostDetailsPresenter, ApiListener {

    private PostDetailsView postDetailsView;
    private ApiManager apiManager;
    private DatabaseRepository databaseRepository;

    private ActivityPostDetailsBinding binding;
    private LayoutInflater layoutInflater;

    private Post post;

    public PostDetailsPresenterImpl(PostDetailsView postDetailsView,
                                    ApiManager apiManager,
                                    DatabaseRepository databaseRepository) {
        this.postDetailsView = postDetailsView;
        this.apiManager = apiManager;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void initialize(ActivityPostDetailsBinding binding, long postId, LayoutInflater layoutInflater) {

        /* STORES BINDING */

        this.binding = binding;

        /* STORES LAYOUT INFLATER */

        this.layoutInflater = layoutInflater;

        /* INITIALIZES VIEWS */

        post = databaseRepository.selectPostById(postId);

        this.binding.setPost(post);
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

        apiManager.requestCommentsByPost(this, postId);
    }

    @Override
    public void onDataLoaded(int process) {
        for(Comment comment : databaseRepository.selectCommentsFromPost(0, post.getId())) {
            printComment(comment);
        }
    }

    private void printComment(Comment comment) {
        addCommentView(comment);
        for(Comment child : comment.getChildren()) {
            printComment(child);
        }
    }

    private void addCommentView(Comment comment) {
        AdapterCommentBinding commentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_comment, null, false);

        commentBinding.setComment(comment);

        /* USER AVATAR */

        Glide.with(binding.getRoot().getContext())
                .load(comment.getUser().getAvatar().getOriginal())
                .asBitmap()
                .centerCrop()
                .transform(new GlideCircleTransform(commentBinding.getRoot().getContext()))
                .into(commentBinding.ivAdapterCommentUser);

        binding.llActivityPostDetailsComments.addView(commentBinding.getRoot());
    }

    @Override
    public void onError(String error) {

    }
}
