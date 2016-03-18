package com.carito.movies;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import com.carito.movies.adapter.ImageAdapter;
import com.carito.movies.api.MovieApi;
import com.carito.movies.api.MovieService;
import com.carito.movies.data.MovieContract;
import com.carito.movies.model.Movie;
import com.carito.movies.util.Constants;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    @Bind(R.id.grid_view) GridView gridView;

    private ImageAdapter imageAdapter;

    private String mSortBy = Constants.POPULARITY_DESC;
    private ArrayList<Movie> mMovies = null;

    private Movie.Response mResponse;
    private MovieApi mMovieApi;
    private int mPages = 1;

    public static final int MAX_PAGES = 2;

    private boolean serviceFlag = false;
    private boolean firstCallService = false;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_BACKDROP_URL,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_ORIGINAL_TITLE = 2;
    public static final int COL_POSTER_URL = 3;
    public static final int COL_BACKDROP_URL = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_VOTE_AVERAGE = 6;
    public static final int COL_RELEASE_DATE = 7;

    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    public MovieFragment() {
    }

    public interface CallbackMovie {
        void onItemSelected(Movie movie);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public  void onSaveInstanceState(Bundle outState){
        if(!mSortBy.contentEquals(Constants.POPULARITY_DESC)){
            outState.putString(Constants.SORT_KEY,mSortBy);
        }
        if(mMovies!=null){
            outState.putParcelableArrayList(Constants.MOVIES_KEY,mMovies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings_popular:
                if(!serviceFlag){
                    mSortBy = Constants.POPULARITY_DESC;
                    loadMovies(mPages,mSortBy);

                    mPages = 1;
                    serviceFlag = true;
                    firstCallService = false;
                }
                return true;
            case R.id.settings_highest:
                if(!serviceFlag){
                    mSortBy = Constants.RATING_DESC;
                    loadMovies(mPages,mSortBy);

                    mPages = 1;
                    serviceFlag = true;
                    firstCallService = false;
                }
                return true;
            case R.id.settings_favorites:
                mSortBy = Constants.FAVORITE;
                loadMovies(1,mSortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstCallService = true;
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        imageAdapter = new ImageAdapter(getActivity(),
                R.layout.movie_item,
                R.id.image_View,
                new ArrayList<Movie>());
        gridView.setAdapter(imageAdapter);
        loadMovies(mPages, "");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Movie movie = imageAdapter.getItem(position);
                ((CallbackMovie) getActivity()).onItemSelected(movie);
                if (movie == null) {
                    return;
                }
            }
        });

/*        gridView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if ((totalItemCount - visibleItemCount == firstVisibleItem) && mPages <= MAX_PAGES) {
                            Log.v(LOG_TAG, "Movie-Page " + mPages);
                            loadPages();
                        }
                    }
                }
        );*/

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.SORT_KEY)) {
                mSortBy = savedInstanceState.getString(Constants.SORT_KEY);
            }

            if (savedInstanceState.containsKey(Constants.MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(Constants.MOVIES_KEY);
                imageAdapter.setMovies(mMovies);
            }/* else {
                loadMovies(mPages,mSortBy);
            }*/
        }/* else {
            loadMovies(mPages,mSortBy);
        }*/
        return rootView;
    }
    private void loadMovies(int num_page,String sortBy){
        if(!sortBy.contentEquals(Constants.FAVORITE)){
            new FetchMovieTask().execute(num_page,sortBy);
        }else{
            new FetchFavoriteTask(getActivity()).execute();
        }
    }
    private void loadPages(){
        if (mPages >= MAX_PAGES) {
            return;
        }
        firstCallService = true;
        loadMovies(++mPages,"");
    }

    public class FetchMovieTask extends AsyncTask{
        private final String LOG_MOVIE = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Object response) {
            if(mResponse == null){
                mResponse = (Movie.Response)response;
                imageAdapter = new ImageAdapter(getActivity(),
                        R.layout.movie_item,
                        R.id.image_View,
                        mResponse.getResults());
                gridView.setAdapter(imageAdapter);
                gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            }else{
                if (imageAdapter != null) {
                    if(mPages >1){
                        imageAdapter.addMovies(((Movie.Response)response).getResults());
                    }else{
                        imageAdapter.updateMovies(((Movie.Response)response).getResults());
                    }
                }
                /*imageAdapter = (ImageAdapter)gridView.getAdapter();*/

                mMovies = new ArrayList<>();
                mMovies.addAll(((Movie.Response)response).getResults());

                serviceFlag = false;
                firstCallService = false;

                Log.v(LOG_MOVIE, "onPostExecute " + mResponse.getResults().size());
            }
        }
        @Override
        protected Movie.Response doInBackground(Object[] params) {
            Movie.Response response;
            if (params.length == 0){
                return null;
            }
            if(mMovieApi==null){
                mMovieApi = MovieService.getMovieApi();
            }
            response = firstCallService? mMovieApi.listMovies((int) params[0]) : mMovieApi.listPreferencesMovies((int) params[0], (String) params[1]);
            System.out.println("response: "+response);
            Log.v(LOG_MOVIE, "Service tmdb" + response.getResults().size());
            return response;
        }
    }

    public class FetchFavoriteTask extends AsyncTask<Void,Void,ArrayList<Movie>>{
        private final String LOG_FAVORITE = FetchFavoriteTask.class.getSimpleName();
        private Context mContext;

        public FetchFavoriteTask(Context context){
            mContext = context;
        }

        private ArrayList<Movie> getFavorite(Cursor cursor){
            ArrayList<Movie> result = new ArrayList<>();
            if(cursor != null && cursor.moveToFirst()){
                do {
                    Movie movie = new Movie(cursor);
                    result.add(movie);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavorite(cursor);
        }

        protected void onPostExecute(ArrayList<Movie> movies) {
            if(movies != null){
                if(imageAdapter != null){
                    imageAdapter.setMovies(movies);
                }
                mMovies = new ArrayList<>();
                mMovies.addAll(movies);
                Log.v(LOG_FAVORITE, "onPostExecute " + mMovies);
            }
        }
    }
}
