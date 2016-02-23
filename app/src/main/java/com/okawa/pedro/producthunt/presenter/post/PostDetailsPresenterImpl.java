package com.okawa.pedro.producthunt.presenter.post;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.databinding.ActivityPostDetailsBinding;
import com.okawa.pedro.producthunt.databinding.AdapterCommentBinding;
import com.okawa.pedro.producthunt.databinding.PlaceholderCommentBinding;
import com.okawa.pedro.producthunt.model.event.ConnectionEvent;
import com.okawa.pedro.producthunt.ui.post.PostDetailsView;
import com.okawa.pedro.producthunt.util.adapter.vote.AdapterVote;
import com.okawa.pedro.producthunt.util.helper.GlideCircleTransform;
import com.okawa.pedro.producthunt.util.listener.ApiListener;
import com.okawa.pedro.producthunt.util.listener.OnRecyclerViewListener;
import com.okawa.pedro.producthunt.util.listener.OnTouchListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;
import com.okawa.pedro.producthunt.util.manager.CallManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import greendao.Comment;
import greendao.Post;
import greendao.Vote;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostDetailsPresenterImpl implements PostDetailsPresenter, ApiListener, OnTouchListener {

    private static final int INDENTATION_RATE = 64;

    private PostDetailsView postDetailsView;
    private ApiManager apiManager;
    private DatabaseRepository databaseRepository;

    private boolean active;

    private ActivityPostDetailsBinding binding;
    private Context context;
    private LayoutInflater layoutInflater;

    private AdapterVote adapterVote;
    private LinearLayoutManager linearLayoutManager;
    private OnVotesRecyclerViewListener onVotesRecyclerViewListener;

    private Post post;

    private int flexibleSpaceHeight;
    private int statusBarColor;
    private int toolbarColor;
    private int titleColor;

    private int totalCommentsAdded;

    public PostDetailsPresenterImpl(PostDetailsView postDetailsView,
                                    ApiManager apiManager,
                                    DatabaseRepository databaseRepository) {
        this.postDetailsView = postDetailsView;
        this.apiManager = apiManager;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void initialize(ActivityPostDetailsBinding binding, long postId, LayoutInflater layoutInflater) {

        /* ACTIVATE */

        active = true;

        /* STORES BINDING */

        this.binding = binding;

        /* CONTEXT */

        context = binding.getRoot().getContext();

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

        /* VOTES */

        adapterVote = new AdapterVote(new ArrayList<Vote>());
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        onVotesRecyclerViewListener = new OnVotesRecyclerViewListener(linearLayoutManager);


        binding.rvActivityPostDetailsVotes.setAdapter(adapterVote);
        binding.rvActivityPostDetailsVotes.setLayoutManager(linearLayoutManager);

        /* TOOLBAR */

        postDetailsView.initializeToolbar(post.getName());

        binding.svActivityPostDetails.setScrollViewCallbacks(new ObservableScrollListener());
        ScrollUtils.addOnGlobalLayoutListener(binding.getRoot(), new GlobalLayoutListener());

        flexibleSpaceHeight = context.getResources().getDimensionPixelSize(R.dimen.activity_main_navigation_header_height);
        toolbarColor = ContextCompat.getColor(context, R.color.color_primary_dark);
        titleColor = ContextCompat.getColor(context, R.color.white);
        statusBarColor = ContextCompat.getColor(context, R.color.black);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            /* CREATES THE LINK TO THE TRANSITION ANIMATION */

            ViewCompat.setTransitionName(binding.viewPostDetails.llViewPostDetails, CallManager.TRANSITION_POST_VIEW);

            /* MAKES STATUS BAR TRANSPARENT */

            postDetailsView.initializeStatusBar();
        }

        /* COMMENTS */

        addPlaceholderView();

        /* RESET LAST COMMENT AND VOTE ID */

        databaseRepository.resetLastCommentId();
        databaseRepository.resetLastVoteId();

        /* REQUEST DATA  */

        requestVotes();
        requestComments();
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Subscribe
    public void onEvent(ConnectionEvent event) {

    }

    @Override
    public void onDataLoaded(int process) {
        if(active) {
            if (process == ApiManager.PROCESS_COMMENTS_ID) {
                for (Comment comment : databaseRepository.selectCommentsFromPost(post.getId(), totalCommentsAdded)) {
                    showComment(comment, 0);
                }

                postDetailsView.onComplete();
            } else if (process == ApiManager.PROCESS_VOTES_ID) {
                adapterVote.addDataSet(databaseRepository.selectVotesFromPost(post.getId(), adapterVote.getItemCount()));
            }
        }
    }

    @Override
    public void onError(String error) {
        postDetailsView.onErrorComments(error);
    }

    /* REQUESTS */

    private void requestVotes() {
        postDetailsView.onRequest();
        apiManager.requestVotesByPost(this, post.getId());
    }

    private void requestComments() {
        postDetailsView.onRequest();
        apiManager.requestCommentsByPost(this, post.getId());
    }

    /* COMMENTS */

    private void showComment(Comment comment, int indentation) {
        addCommentView(comment, indentation);
        indentation += INDENTATION_RATE;
        if(comment.containsChildren()) {
            for (Comment child : comment.getChildren()) {
                showComment(child, indentation);
            }
        }
    }

    private void addCommentView(Comment comment, int indentation) {
        AdapterCommentBinding commentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_comment, null, false);

        commentBinding.setComment(comment);

        /* USER AVATAR */

        Glide.with(binding.getRoot().getContext())
                .load(comment.getUser().getAvatar().getOriginal())
                .asBitmap()
                .centerCrop()
                .transform(new GlideCircleTransform(commentBinding.getRoot().getContext()))
                .into(commentBinding.ivAdapterCommentUser);

        binding.llActivityPostDetailsComments.addView(commentBinding.getRoot(), totalCommentsAdded++);

        int cardMargin = context.getResources().getDimensionPixelSize(R.dimen.card_margin);

        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) commentBinding.getRoot().getLayoutParams();
        p.setMargins(indentation + cardMargin, cardMargin, cardMargin, cardMargin);
        commentBinding.getRoot().requestLayout();
    }

    private void addPlaceholderView() {
        String message;

        if(post.getCommentsCount() == 0) {
            message = context.getString(R.string.post_details_activity_comments_empty);
        } else if(post.getCommentsCount() > totalCommentsAdded) {
            message = context.getString(R.string.post_details_activity_comments_load);
        } else {
            return;
        }

        PlaceholderCommentBinding placeholderCommentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.placeholder_comment, null, false);

        placeholderCommentBinding.setMessage(message);
        placeholderCommentBinding.setTouchListener(this);

        binding.llActivityPostDetailsComments.addView(placeholderCommentBinding.getRoot());

        int cardMargin = context.getResources().getDimensionPixelSize(R.dimen.card_margin);

        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) placeholderCommentBinding.getRoot().getLayoutParams();
        p.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);
        placeholderCommentBinding.getRoot().requestLayout();
    }

    /* TOOLBAR ANIMATIONS */

    private void updateScroll(int scrollY) {
        float alpha = Math.min(1, (float) scrollY / (flexibleSpaceHeight - binding.viewPostDetails.llViewPostDetailsCardInfo.getHeight() - 25));
        binding.toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, toolbarColor));
        binding.toolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, titleColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postDetailsView.changeStatusBarColor(ScrollUtils.getColorWithAlpha(alpha / 4, statusBarColor));
        }

        binding.viewPostDetails.llViewPostDetailsCardInfo.setAlpha(1 - ((float) scrollY / (flexibleSpaceHeight / 3)));
    }

    @Override
    public void onViewTouched(View view) {
        if(view.getId() == R.id.tvPlaceholderComment) {
            requestComments();
        }
    }

    public class GlobalLayoutListener implements Runnable {
        @Override
        public void run() {
            updateScroll(binding.svActivityPostDetails.getCurrentScrollY());
        }
    }

    public class ObservableScrollListener implements ObservableScrollViewCallbacks {
        @Override
        public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            updateScroll(scrollY);
        }

        @Override
        public void onDownMotionEvent() {
        }

        @Override
        public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        }
    }

    protected class OnVotesRecyclerViewListener extends OnRecyclerViewListener {

        public OnVotesRecyclerViewListener(LinearLayoutManager linearLayoutManager) {
            super(linearLayoutManager);
        }

        @Override
        public void onVisibleThreshold() {
            requestVotes();
        }
    }
}
