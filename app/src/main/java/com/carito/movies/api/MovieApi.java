package com.carito.movies.api;

import com.carito.movies.model.Movie;
import com.carito.movies.model.Review;
import com.carito.movies.model.Trailer;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by carito on 10/28/15.
 */
public interface MovieApi {

    @GET("/movie/now_playing")
    Movie.Response listMovies(
            @Query("page") int page);

    @GET("/discover/movie")
    Movie.Response listPreferencesMovies(
            @Query("page") int page,
            @Query("sort_by") String sort_by);

    @GET("/movie/{id}/videos")
    Trailer.Response getTrailersResults(
            @Path("id") int movieId);

    @GET("/movie/{id}/reviews")
    Review.Response getReviewsResults(
            @Path("id") int movieId);
}
