package com.okawa.pedro.producthunt.util.adapter.vote;

import com.bumptech.glide.Glide;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.AdapterVoteBinding;
import com.okawa.pedro.producthunt.util.adapter.common.BindingAdapter;
import com.okawa.pedro.producthunt.util.helper.GlideCircleTransform;

import java.util.List;

import greendao.Vote;

/**
 * Created by pokawa on 22/02/16.
 */
public class AdapterVote extends BindingAdapter<Vote, AdapterVoteBinding> {

    public AdapterVote(List<Vote> data) {
        super(data);
    }

    @Override
    protected int layoutToInflate(int viewType) {
        return R.layout.adapter_vote;
    }

    @Override
    protected void doOnBindViewHolder(BindViewHolder bindViewHolder, AdapterVoteBinding binding, Vote vote, int position) {

        /* DEFINE BINDING USER VARIABLE */

        binding.setVote(vote);

        /* USER AVATAR */

        Glide.with(binding.getRoot().getContext())
                .load(vote.getUser().getAvatar().getOriginal())
                .asBitmap()
                .centerCrop()
                .transform(new GlideCircleTransform(binding.getRoot().getContext()))
                .into(binding.ivAdapterCommentUser);
    }
}
