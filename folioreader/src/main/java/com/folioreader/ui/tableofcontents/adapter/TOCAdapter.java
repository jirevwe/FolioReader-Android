package com.folioreader.ui.tableofcontents.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.folioreader.Config;
import com.folioreader.R;
import com.folioreader.model.TOCLinkWrapper;
import com.folioreader.util.MultiLevelExpIndListAdapter;

import java.util.ArrayList;

/**
 * Created by mahavir on 3/10/17.
 */

public class TOCAdapter extends MultiLevelExpIndListAdapter {

    private static final int LEVEL_ONE_PADDING_PIXEL = 15;
    private final Context mContext;
    private TOCCallback callback;
    private String selectedHref;
    private Config mConfig;

    public TOCAdapter(Context context, ArrayList<TOCLinkWrapper> tocLinkWrappers, String selectedHref, Config config) {
        super(tocLinkWrappers);
        mContext = context;
        this.selectedHref = selectedHref;
        this.mConfig = config;
    }

    private static int getPaddingPixels(Context context, int dpValue) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dpValue * scale + 0.5f);
    }

    public void setCallback(TOCCallback callback) {
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TOCRowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_table_of_contents, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TOCRowViewHolder viewHolder = (TOCRowViewHolder) holder;
        TOCLinkWrapper tocLinkWrapper = (TOCLinkWrapper) getItemAt(position);

        if (tocLinkWrapper.getChildren() == null || tocLinkWrapper.getChildren().isEmpty()) {
            viewHolder.children.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.children.setVisibility(View.VISIBLE);
        }
        viewHolder.sectionTitle.setText(tocLinkWrapper.getTocLink().bookTitle);

        switch (mConfig.getColorMode()) {
            case white:
                if (tocLinkWrapper.isGroup()) {
                    viewHolder.children.setImageResource(R.drawable.ic_plus_black_24dp);
                } else {
                    viewHolder.children.setImageResource(R.drawable.ic_minus_black_24dp);
                }
                break;
            case black:
                if (tocLinkWrapper.isGroup()) {
                    viewHolder.children.setImageResource(R.drawable.ic_plus_white_24dp);
                } else {
                    viewHolder.children.setImageResource(R.drawable.ic_minus_white_24dp);
                }
                break;
            case beige:
                break;
            default:
                break;
        }

        int leftPadding = getPaddingPixels(mContext, LEVEL_ONE_PADDING_PIXEL) * (tocLinkWrapper.getIndentation());
        viewHolder.view.setPadding(leftPadding, 0, 0, 0);

        // set color to each indentation level
        if (tocLinkWrapper.getIndentation() == 0) {
            viewHolder.view.setBackgroundColor(Color.WHITE);
            viewHolder.sectionTitle.setTextColor(Color.BLACK);
        } else if (tocLinkWrapper.getIndentation() == 1) {
            viewHolder.view.setBackgroundColor(Color.parseColor("#f7f7f7"));
            viewHolder.sectionTitle.setTextColor(Color.BLACK);
        } else if (tocLinkWrapper.getIndentation() == 2) {
            viewHolder.view.setBackgroundColor(Color.parseColor("#b3b3b3"));
            viewHolder.sectionTitle.setTextColor(Color.WHITE);
        } else if (tocLinkWrapper.getIndentation() == 3) {
            viewHolder.view.setBackgroundColor(Color.parseColor("#f7f7f7"));
            viewHolder.sectionTitle.setTextColor(Color.BLACK);
        }

        if (tocLinkWrapper.getChildren() == null || tocLinkWrapper.getChildren().isEmpty()) {
            viewHolder.children.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.children.setVisibility(View.VISIBLE);
        }

        switch (mConfig.getColorMode()) {
            case white:
                viewHolder.container.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.white));
                viewHolder.children.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.white));
                viewHolder.sectionTitle.setTextColor(ContextCompat.getColor(mContext,
                        R.color.black));
                break;
            case black:
                viewHolder.container.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.black));
                viewHolder.children.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.black));
                viewHolder.sectionTitle.setTextColor(ContextCompat.getColor(mContext,
                        R.color.white));
                break;
            case beige:
                viewHolder.container.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.beige));
                viewHolder.children.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.beige));
                viewHolder.sectionTitle.setTextColor(ContextCompat.getColor(mContext,
                        R.color.text_color));
                break;
            default:
                break;
        }

        if (tocLinkWrapper.getTocLink().href.equals(selectedHref)) {
            viewHolder.sectionTitle.setTextColor(ContextCompat.getColor(mContext, mConfig.getThemeColor()));
        }
    }

    public interface TOCCallback {
        void onTocClicked(int position);

        void onExpanded(int position);
    }

    public class TOCRowViewHolder extends RecyclerView.ViewHolder {
        public ImageView children;
        TextView sectionTitle;
        private LinearLayout container;
        private View view;

        TOCRowViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            children = (ImageView) itemView.findViewById(R.id.children);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            children.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onExpanded(getAdapterPosition());
                }
            });

            sectionTitle = (TextView) itemView.findViewById(R.id.section_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onTocClicked(getAdapterPosition());
                }
            });
        }
    }
}
