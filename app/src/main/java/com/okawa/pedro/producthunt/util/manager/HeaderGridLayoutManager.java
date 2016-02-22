package com.okawa.pedro.producthunt.util.manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.okawa.pedro.producthunt.util.adapter.common.HeaderAdapter;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

/**
 * Created by pokawa on 22/02/16.
 */
public class HeaderGridLayoutManager extends GridLayoutManager {

    private Context context;
    private ConfigHelper configHelper;
    private HeaderAdapter headerAdapter;

    public HeaderGridLayoutManager(Context context, ConfigHelper configHelper, HeaderAdapter headerAdapter) {
        super(context, configHelper.defineSpanCount(context));
        this.context = context;
        this.configHelper = configHelper;
        this.headerAdapter = headerAdapter;

        setSpanSizeLookup(new HeaderGridSpanLookup());
    }

    public class HeaderGridSpanLookup extends SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            if(headerAdapter.isHeader(position)) {
                return configHelper.defineSpanCount(context);
            } else {
                return configHelper.defineSpanItem(context);
            }
        }
    }
}
