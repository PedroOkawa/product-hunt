package com.okawa.pedro.producthunt.util.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.AdapterPostBinding;
import com.okawa.pedro.producthunt.model.event.PostSelectEvent;
import com.okawa.pedro.producthunt.util.helper.GlideCircleTransform;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import greendao.Post;

/**
 * Created by pokawa on 20/02/16.
 */
public class AdapterPost extends BindingAdapter<Post, AdapterPostBinding> {

    public AdapterPost(List<Post> data) {
        super(data);
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.adapter_post;
    }

    @Override
    protected void doOnBindViewHolder(BindViewHolder bindViewHolder, final AdapterPostBinding binding, final Post post, int position) {

        /* DEFINE BINDING POST VARIABLE */

        binding.viewPostDetails.setPost(post);

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

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PostSelectEvent(post.getId(), binding.viewPostDetails.llViewPostDetails));
            }
        });
    }
}
