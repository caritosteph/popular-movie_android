package com.carito.movies.util;

import android.content.Context;
import android.database.Cursor;

import com.carito.movies.data.MovieContract;

/**
 * Created by carito on 2/20/16.
 */
public class Utility {
    public static int isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?",
                new String[]{Integer.toString(id)},
                null
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }
}
