package com.carito.movies.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.carito.movies.MovieFragment;
import com.carito.movies.util.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by carito on 10/28/15.
 */
public class Movie implements Parcelable {

    @Expose
    int id;

    @Expose @SerializedName("genre_ids")
    ArrayList<Integer> genreIds = new ArrayList<>();

    @Expose
    String overview;

    @Expose @SerializedName("release_date")
    String releaseDate;

    @Expose @SerializedName("poster_path")
    String posterPath;

    @Expose @SerializedName("backdrop_path")
    String backdropPath;

    @Expose
    double popularity;

    @Expose
    String title;

    @Expose @SerializedName("vote_average")
    double voteAverage;

    @Expose @SerializedName("vote_count")
    int voteCount;

    public Movie() {}

    public int getId() {
        return id;
    }

    public Movie setId(int id) {
        this.id = id;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Movie setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public Movie setGenreIds(ArrayList<Integer> genreIds) {
        this.genreIds = genreIds;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate(){
        if (!TextUtils.isEmpty(releaseDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.MOVIE_IN_REALESE_FORMAT);
            try {
                Date date = simpleDateFormat.parse(releaseDate);
                SimpleDateFormat outputDateFormat = new SimpleDateFormat(Constants.MOVIE_OUT_REALESE_FORMAT);
                return outputDateFormat.format(date);
            } catch (ParseException e) {
                Log.e("MovieData", "getFormattedDate() returned error: " + e);
            }
        }
        return null;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public double getPopularity() {
        return popularity;
    }

    public Movie setPopularity(double popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getVoteAverage() {

        return " "+Math.round(voteAverage)+"/10";
    }

    public Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Movie setVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(MovieFragment.COL_MOVIE_ID);
        this.title = cursor.getString(MovieFragment.COL_ORIGINAL_TITLE);
        this.posterPath = cursor.getString(MovieFragment.COL_POSTER_URL);
        this.backdropPath = cursor.getString(MovieFragment.COL_BACKDROP_URL);
        this.overview = cursor.getString(MovieFragment.COL_OVERVIEW);
        this.voteAverage = cursor.getInt(MovieFragment.COL_VOTE_AVERAGE);
        this.releaseDate = cursor.getString(MovieFragment.COL_RELEASE_DATE);
    }

    @Override
    public String toString() {
        return "Movie{" + " title='" + title + '}';
    }

    public Uri buildPosterUri(String imgURL,String size,String posterPath) {
        Uri builtUri = Uri.parse(imgURL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(posterPath)
                .build();
        return builtUri;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeList(this.genreIds);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.title);
        dest.writeDouble(this.voteAverage);
        dest.writeInt(this.voteCount);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, List.class.getClassLoader());
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
        this.voteCount = in.readInt();
    }

    public static final class Response {

        @Expose
        public int page;

        @Expose @SerializedName("total_pages")
        public int totalPages;

        @Expose @SerializedName("total_results")
        public int totalMovies;

        @Expose @SerializedName("results")
        public ArrayList<Movie> movies = new ArrayList<>();


        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public ArrayList<Movie> getResults() {
            return movies;
        }

        public void setResults(ArrayList<Movie> results) {
            this.movies = results;
        }
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {return new Movie(source);}

        public Movie[] newArray(int size) {return new Movie[size];}
    };
}
