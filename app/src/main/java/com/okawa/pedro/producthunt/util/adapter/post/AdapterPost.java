package com.okawa.pedro.producthunt.util.adapter.post;

import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.AdapterHeaderPostBinding;
import com.okawa.pedro.producthunt.databinding.AdapterPostBinding;
import com.okawa.pedro.producthunt.model.event.PostSelectEvent;
import com.okawa.pedro.producthunt.model.list.PostContent;
import com.okawa.pedro.producthunt.util.adapter.common.BindingAdapter;
import com.okawa.pedro.producthunt.util.adapter.common.HeaderAdapter;
import com.okawa.pedro.producthunt.util.helper.GlideCircleTransform;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import greendao.Post;

/**
 * Created by pokawa on 20/02/16.
 */
public class AdapterPost extends HeaderAdapter<PostContent, ViewDataBinding> {

    public AdapterPost(List<PostContent> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader() ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public boolean isHeader(int position) {
        return getItem(position).isHeader();
    }

    @Override
    protected int layoutToInflate(int viewType) {
        return viewType == VIEW_TYPE_HEADER ? R.layout.adapter_header_post : R.layout.adapter_post;
    }

    @Override
    protected void doOnBindViewHolder(BindViewHolder bindViewHolder, ViewDataBinding binding, PostContent postContent, int position) {

        if(postContent.isHeader()) {

            AdapterHeaderPostBinding adapterHeaderPostBinding = (AdapterHeaderPostBinding) binding;
            adapterHeaderPostBinding.setHeader(postContent.getHeader());

        } else {

            final Post post = postContent.getPost();
            final AdapterPostBinding adapterPostBinding = (AdapterPostBinding) binding;

            /* DEFINE BINDING POST VARIABLE */

            adapterPostBinding.viewPostDetails.setPost(post);

            /* POST PREVIEW */

            Glide.with(binding.getRoot().getContext())
                    .load(post.getThumbnail().getImage())
                    .asBitmap()
                    .centerCrop()
                    .into(adapterPostBinding.viewPostDetails.ivViewPostDetailsPreview);

            /* USER AVATAR */

            Glide.with(binding.getRoot().getContext())
                    .load(post.getUser().getAvatar().getOriginal())
                    .asBitmap()
                    .centerCrop()
                    .transform(new GlideCircleTransform(binding.getRoot().getContext()))
                    .into(adapterPostBinding.viewPostDetails.ivViewPostDetailsUser);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new PostSelectEvent(post.getId(), adapterPostBinding.viewPostDetails.llViewPostDetails));
                }
            });
        }
    }

}
