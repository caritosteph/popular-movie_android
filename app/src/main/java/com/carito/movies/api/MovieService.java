package com.carito.movies.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

/**
 * Created by carito on 10/28/15.
 */
public class MovieService{
    private static MovieApi movieApi;
    public static final String MOVIE_DB_API_URL = "http://api.themoviedb.org/3";
    private static final String API_KEY = "";
    private static final String TAG = "MovieService";

    public static MovieApi getMovieApi() {

        if (movieApi == null) {
            Gson gson = new GsonBuilder().create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(MOVIE_DB_API_URL)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addQueryParam("api_key", API_KEY);
                            request.addQueryParam("format", "json");
                        }
                    })
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new AndroidLog(TAG))
                    .build();

            movieApi = restAdapter.create(MovieApi.class);
            return movieApi;
        }
        return movieApi;

    }
}
