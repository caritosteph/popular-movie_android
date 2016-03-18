package com.carito.movies.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carito.movies.R;
import com.carito.movies.model.Movie;
import com.carito.movies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by carito on 2/28/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private final Context mContext;
    private final Trailer trailer = new Trailer();
    private ArrayList<Trailer> mTrailer;
    LayoutInflater inflater ;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        mContext = context;
        mTrailer = trailers;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return mContext;
    }

    public void addTrailer(ArrayList<Trailer> movie_trailer){
        if (movie_trailer == null) {
            movie_trailer = new ArrayList<>();
        }
        mTrailer = movie_trailer;
        notifyDataSetChanged();
    }

/*    public void add(Trailer object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }*/

/*    public void updateMovies (ArrayList<Trailer> movie_trailer){
        this.mTrailer.clear();
        this.mTrailer = movie_trailer;
        notifyDataSetChanged();
    }*/

    public void clear (){
        synchronized (trailer) {
            mTrailer.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTrailer.size();
    }

    @Override
    public Trailer getItem(int position) {
        return mTrailer.get(position);
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
            view = inflater.inflate(R.layout.movie_trailer, parent,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();

        Uri trailerUri = null;
        if (getItem(position).getKey() != null) {
            trailerUri = getItem(position).buildPosterTrailer(mContext.getResources().getString(R.string.youtube_image), getItem(position).getKey());
        }
        Picasso.with(mContext)
                .load(trailerUri)
                .into(viewHolder.trailer_image);

        viewHolder.trailer_name.setText(getItem(position).getName());
        return view;
    }
    public static class ViewHolder{
        @Bind(R.id.trailer_image) ImageView trailer_image;
        @Bind(R.id.trailer_name) TextView trailer_name;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
