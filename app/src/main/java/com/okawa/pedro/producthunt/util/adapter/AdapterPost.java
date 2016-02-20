package com.okawa.pedro.producthunt.util.adapter;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.AdapterPostBinding;

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
    protected void doOnBindViewHolder(BindViewHolder bindViewHolder, AdapterPostBinding binding, Post item, int position) {
        Glide.with(binding.getRoot().getContext())
                .load(item.getThumbnail().getImage())
                .dontAnimate()
                .centerCrop()
                .into(binding.viewPostDetails.ivViewPostDetailsPreview);
    }
}
