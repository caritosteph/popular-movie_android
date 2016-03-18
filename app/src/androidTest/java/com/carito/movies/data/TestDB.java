package com.carito.movies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by carito on 12/5/15.
 */
public class TestDB extends AndroidTestCase{

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    public void setUp(){
        deleteDataBase();
    }

    public void deleteDataBase(){
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

//    public void testCreateDb() throws Throwable {
//        // build a HashSet of all of the table names we wish to look for
//        // Note that there will be another table in the DB that stores the
//        // Android metadata (db version information)
//        final HashSet<String> tableNameHashSet = new HashSet<String>();
//        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
//
////        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
//        SQLiteDatabase movieDb = new MovieDbHelper(this.mContext).getWritableDatabase();
//        assertEquals(true, movieDb.isOpen());
//
//        // have we created the tables we want?
//        Cursor c = movieDb.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());
//
//        // verify that the tables have been created
//        do {
//            tableNameHashSet.remove(c.getString(0));
//        } while (c.moveToNext());
//
////        assertTrue("Error: Your database was created without movie entry table", tableNameHashSet.isEmpty());
//
//        // now, do our tables contain the correct columns?
//        c = movieDb.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);
//
////        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());
//
//        // Build a HashSet of all of the column names we want to look for
//        final HashSet<String> movieColumnHashSet = new HashSet<String>();
//        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_URL);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_BACKDROP_URL);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_AVERAGE_RATE);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_GENRE);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_COUNTRY);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_FAVORITE);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOST_POPULAR);
//        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_HIGHEST_RATED);
//
//        int columnNameIndex = c.getColumnIndex("name");
//        do {
//            String columnName = c.getString(columnNameIndex);
//            movieColumnHashSet.remove(columnName);
//        } while (c.moveToNext());
//
//        // if this fails, it means that your database doesn't contain all of the required location
//        // entry columns
//        assertTrue("Error: The database doesn't contain all of the required movie entry columns", movieColumnHashSet.isEmpty());
//        movieDb.close();
//    }
//
//    public void testMovieTable() {
//
//        // First step: Get reference to writable database
//        // If there's an error in those massive SQL table creation Strings,
//        // errors will be thrown here when you try to get a writable database.
//        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Second Step: Create ContentValues of what you want to insert
//        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
//
//        // Third Step: Insert ContentValues into database and get a row ID back
//        long locationRowId;
//        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);
//
//        // Verify we got a row back.
//        assertTrue(locationRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // Fourth Step: Query the database and receive a Cursor back
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = db.query(
//                MovieContract.MovieEntry.TABLE_NAME,  // Table to Query
//                null, // all columns
//                null, // Columns for the "where" clause
//                null, // Values for the "where" clause
//                null, // columns to group by
//                null, // columns to filter by row groups
//                null // sort order
//        );
//
//        // Move the cursor to a valid database row and check to see if we got any records back
//        // from the query
//        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());
//
//        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
//        // (you can use the validateCurrentRecord function in TestUtilities to validate the
//        // query if you like)
//        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
//                cursor, testValues);
//
//        // Move the cursor to demonstrate that there is only one record in the database
//        assertFalse( "Error: More than one record returned from location query",
//                cursor.moveToNext() );
//
//        // Sixth Step: Close Cursor and Database
//        cursor.close();
//        db.close();
//    }
}
