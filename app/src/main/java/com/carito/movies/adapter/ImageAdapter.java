package com.carito.movies.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.carito.movies.R;
import com.carito.movies.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by carito on 10/28/15.
 */
public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Movie> mMovies;
    private int item_layout;
    private int item_id;

    public ImageAdapter() {
    }

    public ImageAdapter(Context c,int item_layout,int item_id,ArrayList<Movie> movies) {
        this.mContext = c;
        this.item_layout = item_layout;
        this.item_id = item_id;
        this.mMovies = movies;
    }

    public void setMovies(ArrayList<Movie> mv){
        this.mMovies = mv;
    }

    public void addMovies(ArrayList<Movie> movies){
        synchronized (mMovies) {
            this.mMovies.addAll(movies);
        }
        notifyDataSetChanged();
    }

    public void updateMovies (ArrayList<Movie> movies){
        this.mMovies.clear();
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        if (position < 0 || position >= mMovies.size()) {
            return null;
        }
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        ImageView imageView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            grid = inflater.inflate(R.layout.movie_item, null);
            imageView = (ImageView)grid.findViewById(R.id.image_View);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri posterUri;
        if (getItem(position).getPosterPath() != null) {
            posterUri = getItem(position).buildPosterUri(mContext.getResources().getString(R.string.image_url),
                                                        mContext.getResources().getString(R.string.image_size_w342),
                                                        getItem(position).getPosterPath());
        }else{
            posterUri = getItem(position).buildPosterUri(mContext.getResources().getString(R.string.image_url),
                                                        mContext.getResources().getString(R.string.image_size_w342),
                                                        getItem(position).getBackdropPath());
        }
        if(getItem(position).getPosterPath() == null && getItem(position).getBackdropPath() == null){
            posterUri = Uri.parse("http://placehold.it/342?text=No image available");
        }
        Picasso.with(mContext)
                .load(posterUri)
                .into(imageView);
        return imageView;
    }
}
