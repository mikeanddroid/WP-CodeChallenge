package com.mike.givemewingzz.washingtonpostchallenge.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.mike.givemewingzz.washingtonpostchallenge.R;
import com.mike.givemewingzz.washingtonpostchallenge.customviews.RobotoTextView;
import com.mike.givemewingzz.washingtonpostchallenge.data.model.PostsData;
import com.mike.givemewingzz.washingtonpostchallenge.dateutils.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import io.realm.RealmResults;

public class PostsAdapter extends RealmBaseRecyclerViewAdapter<PostsData, PostsAdapter.PostsViewHolder> {

    public RealmResults<PostsData> realmResults;
    public Context context;
    public EventListener eventListener;

    public PostsAdapter(Context context, RealmResults<PostsData> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_items, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {

        final PostsData postsData = getItem(position);

        String postsTitle = postsData.getPostTitle();
        String postDate = postsData.getPostDate();

        Calendar calendar = null;
        try {
            calendar = DateUtil.parseISODate(postDate, TimeZone.getDefault().getDisplayName());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = DateUtil.formatDate(calendar, DateUtil.DateFormat.COMPLETE_DATE_FORMAT);

        Spanned decodedTitle = Html.fromHtml(postsTitle);

        holder.postTitle.setText(decodedTitle);
        holder.postDate.setText(formattedDate);

        Animation animation;
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        holder.postContentHolder.startAnimation(animation);

        holder.postContentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                eventListener.onItemClick(v, postsData);
            }
        });

    }

    @Override
    public PostsData getItem(int i) {
        return realmResults.get(i);
    }

    public void swapData(RealmResults<PostsData> realmResults) {
        this.realmResults = realmResults;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        RobotoTextView postTitle;
        RobotoTextView postDate;
        LinearLayout postContentHolder;

        PostsViewHolder(View itemView) {
            super(itemView);
            postContentHolder = (LinearLayout) itemView.findViewById(R.id.post_content_holder);
            postTitle = (RobotoTextView) itemView.findViewById(R.id.post_title);
            postDate = (RobotoTextView) itemView.findViewById(R.id.post_date);
        }
    }

    public interface EventListener {
        void onItemClick(final View view, PostsData postsData);
    }

}

