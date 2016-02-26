package com.okawa.pedro.producthunt.presenter.post;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.databinding.ActivityPostDetailsBinding;
import com.okawa.pedro.producthunt.databinding.AdapterCommentBinding;
import com.okawa.pedro.producthunt.model.event.ApiEvent;
import com.okawa.pedro.producthunt.model.event.ConnectionEvent;
import com.okawa.pedro.producthunt.util.builder.ParametersBuilder;
import com.okawa.pedro.producthunt.ui.post.PostDetailsView;
import com.okawa.pedro.producthunt.util.adapter.vote.AdapterVote;
import com.okawa.pedro.producthunt.util.helper.CircleTransform;
import com.okawa.pedro.producthunt.util.listener.OnRecyclerViewListener;
import com.okawa.pedro.producthunt.util.listener.OnTouchListener;
import com.okawa.pedro.producthunt.util.manager.ApiManager;
import com.okawa.pedro.producthunt.util.manager.CallManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;

import greendao.Comment;
import greendao.Post;
import greendao.Vote;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostDetailsPresenterImpl implements PostDetailsPresenter, OnTouchListener {

    private static final int INDENTATION_RATE = 64;

    private PostDetailsView postDetailsView;
    private ApiManager apiManager;
    private ParametersBuilder parametersBuilder;
    private DatabaseRepository databaseRepository;

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
                                    ParametersBuilder parametersBuilder,
                                    DatabaseRepository databaseRepository) {
        this.postDetailsView = postDetailsView;
        this.apiManager = apiManager;
        this.parametersBuilder = parametersBuilder;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void initialize(ActivityPostDetailsBinding binding, long postId, LayoutInflater layoutInflater) {

        /* REGISTER ON EVENT BUS */

        EventBus.getDefault().register(this);

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

        Picasso.with(binding.getRoot().getContext())
                .load(post.getThumbnail().getImage())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .fit()
                .into(binding.viewPostDetails.ivViewPostDetailsPreview);

        /* USER AVATAR */

        Picasso.with(binding.getRoot().getContext())
                .load(post.getUser().getAvatar().getOriginal())
                .placeholder(R.mipmap.ic_user_placeholder)
                .error(R.mipmap.ic_user_placeholder)
                .centerCrop()
                .fit()
                .transform(new CircleTransform())
                .into(binding.viewPostDetails.ivViewPostDetailsUser);

        /* VOTES */

        adapterVote = new AdapterVote(new ArrayList<Vote>());
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        onVotesRecyclerViewListener = new OnVotesRecyclerViewListener(linearLayoutManager);

        binding.rvActivityPostDetailsVotes.setAdapter(adapterVote);
        binding.rvActivityPostDetailsVotes.setLayoutManager(linearLayoutManager);
        binding.rvActivityPostDetailsVotes.addOnScrollListener(onVotesRecyclerViewListener);

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

        binding.setTouchListener(this);
        binding.setMessage(getCommentsButtonMessage());

        /* START ACTIVITY ON TOP (SCROLLVIEW) */

        binding.rvActivityPostDetailsVotes.setFocusable(false);

        /* REQUEST DATA  */

        requestComments();
    }

    @Override
    public void dispose() {

        /* UNREGISTER ON EVENT BUS */

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ConnectionEvent event) {
        requestComments();
        requestVotes();
    }

    @Subscribe
    public void onEvent(ApiEvent apiEvent) {
        if(apiEvent.isError()) {
            postDetailsView.onError(apiEvent.getType(), apiEvent.getMessage());
        } else {
            if (apiEvent.getType() == ApiEvent.PROCESS_COMMENTS_ID) {
                for (Comment comment : databaseRepository.selectCommentsFromPost(post.getId(), totalCommentsAdded)) {
                    showComment(comment, 0);
                }

                binding.setMessage(getCommentsButtonMessage());

                postDetailsView.onComplete();
            } else if (apiEvent.getType() == ApiEvent.PROCESS_VOTES_ID) {
                adapterVote.addDataSet(databaseRepository.selectVotesFromPost(post.getId(), adapterVote.getItemCount()));

                if(adapterVote.getItemCount() == 0) {
                    binding.rvActivityPostDetailsVotes.setVisibility(View.GONE);
                    binding.tvActivityPostDetailsVotesPlaceholder.setVisibility(View.VISIBLE);
                } else {
                    binding.rvActivityPostDetailsVotes.setVisibility(View.VISIBLE);
                    binding.tvActivityPostDetailsVotesPlaceholder.setVisibility(View.GONE);
                }
            }
        }
    }

    /* REQUESTS */

    private void requestVotes() {
        postDetailsView.onRequest();

        /* GENERATE PARAMETERS */

        Map<String, String> parameters = parametersBuilder
                .init()
                .setNewer(databaseRepository.getLastVoteId())
                .setAscending()
                .setPagination()
                .generateParameters();

        apiManager.requestVotesByPost(post.getId(), parameters);
    }

    private void requestComments() {
        postDetailsView.onRequest();

        /* GENERATE PARAMETERS */

        Map<String, String> parameters = parametersBuilder
                .init()
                .setOlder(databaseRepository.getLastCommentId())
                .setDescending()
                .setPagination()
                .generateParameters();

        apiManager.requestCommentsByPost(post.getId(), parameters);
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

        Picasso.with(binding.getRoot().getContext())
                .load(comment.getUser().getAvatar().getOriginal())
                .placeholder(R.mipmap.ic_user_placeholder)
                .error(R.mipmap.ic_user_placeholder)
                .centerCrop()
                .fit()
                .transform(new CircleTransform())
                .into(commentBinding.ivAdapterCommentUser);

        addViewOnCommentLayout(commentBinding.getRoot(), totalCommentsAdded++, indentation);
    }

    private String getCommentsButtonMessage() {
        if(post.getCommentsCount() == 0) {
            return context.getString(R.string.post_details_activity_comments_empty);
        } else {
            return context.getString(R.string.post_details_activity_comments_load);
        }
    }

    /* DYNAMIC VIEW */

    private void addViewOnCommentLayout(View view, int index, int indentation) {
        binding.llActivityPostDetailsComments.addView(view, index);

        int cardMargin = context.getResources().getDimensionPixelSize(R.dimen.card_margin);

        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        p.setMargins(cardMargin + indentation, cardMargin, cardMargin, cardMargin);
        view.requestLayout();

    }

    /* TOOLBAR ANIMATIONS */

    private void updateScroll(int scrollY) {
        float alpha = Math.min(1, (float) scrollY / (flexibleSpaceHeight - binding.viewPostDetails.llViewPostDetailsCardInfo.getHeight() - 25));

        binding.toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, toolbarColor));
        binding.toolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, titleColor));
        binding.toolbarShadow.setAlpha(alpha);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postDetailsView.changeStatusBarColor(ScrollUtils.getColorWithAlpha(alpha / 4, statusBarColor));
        }

        binding.viewPostDetails.llViewPostDetailsCardInfo.setAlpha(1 - ((float) scrollY / (flexibleSpaceHeight / 3)));
    }

    @Override
    public void onViewTouched(View view) {
        if(view.getId() == R.id.tvActivityPostDetailsCommentsButton) {
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
