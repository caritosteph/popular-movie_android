package com.carito.movies;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.carito.movies.adapter.ReviewAdapter;
import com.carito.movies.adapter.TrailerAdapter;
import com.carito.movies.api.MovieApi;
import com.carito.movies.api.MovieService;
import com.carito.movies.data.MovieContract;
import com.carito.movies.model.Movie;
import com.carito.movies.model.Review;
import com.carito.movies.model.Trailer;
import com.carito.movies.util.Utility;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by carito on 11/1/15.
 */
public class DetailFragment extends Fragment {
    public static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    public static ShareActionProvider shareActionProvider;

    private MovieApi mMovieApi;

    private Movie mMovie;
    private Trailer mTrailer;

    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;

    @Bind(R.id.movie_poster) ImageView movie_poster;
    @Bind(R.id.movie_poster_detail) ImageView movie_poster_detail;
    @Bind(R.id.movie_title) TextView movie_title;
    @Bind(R.id.movie_overview) TextView movie_overview;
    @Bind(R.id.movie_release_date) TextView movie_release_date;
    @Bind(R.id.movie_rating) TextView movie_rating;
    @Bind(R.id.movie_trailers) LinearListView movie_trailers_list;
    @Bind(R.id.movie_reviews) LinearListView movie_reviews_list;
    @Bind(R.id.trailer_cardview) CardView trailer_cardview;
    @Bind(R.id.review_cardview) CardView review_cardview;
    @Bind(R.id.detail_layout) ScrollView scrollView;

    public static TrailerAdapter trailerAdapter;
    public static ReviewAdapter reviewAdapter;
    private Toast toast;

    public DetailFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private Intent createShareMovieIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + ": " +
                getString(R.string.youtube_url) + mTrailer.getKey());
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if(mMovie != null){
            inflater.inflate(R.menu.detail_menu, menu);
            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            MenuItem action_share =  menu.findItem(R.id.action_share);

            new AsyncTask<Void,Void,Integer>(){
                @Override
                protected Integer doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(),mMovie.getId());
                }
                protected void onPostExecute(Integer isFavorite){
                    action_favorite.setIcon(isFavorite==1?R.drawable.abc_btn_rating_star_on_mtrl_alpha:R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();

            shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);
            if(mTrailer != null){
                shareActionProvider.setShareIntent(createShareMovieIntent());
            }else{
                Log.d(LOG_TAG,"Share action provider is null ?");
            }
        }
    }

    public boolean onOptionsItemSelected(final MenuItem item){
        final int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_favorite:
                if(mMovie != null) {
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            return Utility.isFavorited(getActivity(), mMovie.getId());
                        }
                        protected void onPostExecute(Integer isFavorite){
                            if (isFavorite == 1){ //delete favorites
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?",
                                                new String[]{Integer.toString(mMovie.getId())});
                                    }
                                    protected void onPostExecute(Integer numRows){
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if(toast != null){
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(getActivity(),getString(R.string.remove_favorites),Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }else{//add
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();
                                        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,mMovie.getId());
                                        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,mMovie.getTitle());
                                        values.put(MovieContract.MovieEntry.COLUMN_POSTER_URL,mMovie.getPosterPath());
                                        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL,mMovie.getBackdropPath());
                                        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,mMovie.getOverview());
                                        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,mMovie.getVoteAverage());
                                        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,mMovie.getReleaseDate());
                                        return getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);
                                    }
                                    protected void onPostExecute(Uri uri){
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if(toast != null){
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(getActivity(),getString(R.string.add_favorites),Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(DetailFragment.DETAIL_MOVIE);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(DETAIL_MOVIE)) {
            mMovie = intent.getParcelableExtra(DETAIL_MOVIE);
            if(mMovie != null){
                scrollView.setVisibility(View.VISIBLE);
            }else {
                scrollView.setVisibility(View.INVISIBLE);
            }

            trailerAdapter = new TrailerAdapter(getActivity(),new ArrayList<Trailer>());
            movie_trailers_list.setAdapter(trailerAdapter);
            movie_trailers_list.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                public void onItemClick(LinearListView linearListView, View view,
                                        int position, long id) {
                    Trailer item_trailer = trailerAdapter.getItem(position);
                    Intent intent_trailer = new Intent(Intent.ACTION_VIEW);
                    intent_trailer.setData(Uri.parse(getString(R.string.youtube_url) + item_trailer.getKey()));
                    startActivity(intent_trailer);
                }
            });

            reviewAdapter = new ReviewAdapter(getActivity(),new ArrayList<Review>());
            movie_reviews_list.setAdapter(reviewAdapter);

            movie_title.setText(mMovie.getTitle());
            movie_rating.setText(mMovie.getVoteAverage());
            movie_overview.setText(mMovie.getOverview());
            movie_release_date.setText(mMovie.getReleaseDate());
            Uri posterImage;
            if (mMovie.getPosterPath() != null) {
                posterImage = mMovie.buildPosterUri(getString(R.string.image_url), getString(R.string.image_size_w342), mMovie.getPosterPath());
            } else {
                posterImage = Uri.parse("http://i.media-imdb.com/images/SFa6c7a966d6dcebed648b97af73c87f0d/nopicture/67x98/film.png");
            }
            Picasso.with(getActivity())
                    .load(posterImage)
                    .into(movie_poster);
            Picasso.with(getActivity())
                    .load(posterImage)
                    .into(movie_poster_detail);
        }
        return rootView;
    }

    public void onStart(){
        super.onStart();
        if (mMovie != null){
            //FetchTrailerTask
            new FetchTrailersTask().execute(mMovie.getId());
            //FetchReviewTask
            new FetchReviewTask().execute(mMovie.getId());
        }
    }

    public class FetchTrailersTask extends AsyncTask{

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        @Override
        protected Trailer.Response doInBackground(Object[] params) {
            Trailer.Response response;
            if(params.length == 0){
                return null;
            }
            if(mMovieApi==null){
                mMovieApi = MovieService.getMovieApi();
            }
            response = mMovieApi.getTrailersResults((int) params[0]);
            Log.v(LOG_TAG, "Service tmdb" + response.getResults().size());
            return response;
        }

        protected void onPostExecute(Object response) {
            if(response != null){
                mTrailers = ((Trailer.Response) response).getResults();
                if (mTrailers.size() > 0) {
                    trailer_cardview.setVisibility(View.VISIBLE);
                    if (trailerAdapter != null) {
                        trailerAdapter.clear();
                        trailerAdapter.addTrailer(mTrailers);
                    }
                    mTrailer = mTrailers.get(0);

                    if (shareActionProvider != null) {
                        shareActionProvider.setShareIntent(createShareMovieIntent());
                    }
                }
            }
        }
    }

    public class FetchReviewTask extends AsyncTask {

        private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

        @Override
        protected Review.Response doInBackground(Object[] params) {
            Review.Response response;
            if(params.length == 0){
                return null;
            }
            if(mMovieApi==null){
                mMovieApi = MovieService.getMovieApi();
            }
            response = mMovieApi.getReviewsResults((int) params[0]);
            Log.v(LOG_TAG, "Service tmdb" + response.getResults().size());
            return response;
        }

        protected void onPostExecute(Object response) {
            if(response != null){
                mReviews = ((Review.Response)response).getResults();
                if(mReviews.size()>0){
                    review_cardview.setVisibility(View.VISIBLE);
                    if(reviewAdapter != null){
                        reviewAdapter.clear();
                        reviewAdapter.addReview(mReviews);
                    }
                }
            }
        }
    }
}
