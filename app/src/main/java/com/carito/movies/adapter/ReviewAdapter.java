package com.carito.movies.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.carito.movies.R;
import com.carito.movies.model.Review;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by carito on 2/28/16.
 */
public class ReviewAdapter extends BaseAdapter {
    private final Context mContext;
    private final Review review = new Review();
    private ArrayList<Review> mReview = new ArrayList<>();;
    LayoutInflater inflater;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        mContext = context;
        mReview = reviews;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return mContext;
    }

    public void addReview(ArrayList<Review> movie_review){
        if (movie_review == null) {
            movie_review = new ArrayList<>();
        }
        mReview = movie_review;
        notifyDataSetChanged();
    }

/*    public void updateReview (ArrayList<Review> movie_reviews){
        this.mReview.clear();
        this.mReview = movie_reviews;
        notifyDataSetChanged();
    }*/

    public void clear (){
        synchronized (review) {
            mReview.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mReview.size();
    }

    @Override
    public Review getItem(int position) {
        return mReview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.movie_review, parent,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.review_author.setText(getItem(position).getAuthor());
        viewHolder.review_content.setText(Html.fromHtml(getItem(position).getContent()));

        return view;
    }

    public static class ViewHolder{
        @Bind(R.id.review_author) TextView review_author;
        @Bind(R.id.review_content) TextView review_content;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
