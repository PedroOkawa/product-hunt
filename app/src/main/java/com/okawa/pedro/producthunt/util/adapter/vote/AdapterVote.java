package com.okawa.pedro.producthunt.util.adapter.vote;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.AdapterVoteBinding;
import com.okawa.pedro.producthunt.util.adapter.common.BindingAdapter;
import com.okawa.pedro.producthunt.util.helper.CircleTransform;
import com.squareup.picasso.Picasso;

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

        Picasso.with(binding.getRoot().getContext())
                .load(vote.getUser().getAvatar().getOriginal())
                .centerCrop()
                .fit()
                .transform(new CircleTransform())
                .into(binding.ivAdapterCommentUser);
    }
}
