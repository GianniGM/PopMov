package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.R;

import static com.example.android.popularmovies.data.MoviesContract.*;

/**
 * Created by giannig on 2/28/17.
 * Content provodider for movies.db
 */

public class MoviesProvider extends ContentProvider {
    //TODO DON'T REMBER IN THE INSERT THE VALUES TRUE/FALSE AND THE APPROPRIATE QUERIES
    public static final int CODE_BEST = 100;
    public static final int CODE_FAVOURITE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int IS_TRUE = 1;

    private MoviesDBHelper mDBHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = CONTENT_AUTHORITY;
        final String path = PATH_MOVIES;

        uriMatcher.addURI(authority, path, CODE_FAVOURITE);
        uriMatcher.addURI(authority, path + "/*", CODE_BEST);

        return uriMatcher;
    }

    /**
     * set contract's flags TOP_RATED and MOST_POPULAR to true and insert into database
     * NOTE: true is equals to value 1,
     *
     * @param db        the database to refers to
     * @param values    values to insert
     * @param flags     flags to set to true
     * @return          number of inserted elements
     */
    public int setFlagAndInsert(SQLiteDatabase db, ContentValues[] values, String...flags){

        int inserted = 0;

        db.beginTransaction();
        try {
            for (ContentValues v :  values) {
                //TODO NOT NORMALIzED

                for(String f : flags) {
                    v.put(f, IS_TRUE);
                }

                long id = db.insert(MovieEntry.NAME_TABLE, null, v);
                if(id != -1){
                    inserted++;
                }
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        return inserted;
    }


    @Override
    public boolean onCreate() {
        mDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_BEST: {
                //bulk insertions are safe
                db.beginTransaction();

                String valueEntry = uri.getLastPathSegment();
                int insertedRow = setFlagAndInsert(db, values, valueEntry);

                if (insertedRow > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return insertedRow;
            }

            //TODO FARE CASE FAVOURITE

            default:
               return super.bulkInsert(uri, values);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

//        String[] selectionArguments = new String[]{uri.getLastPathSegment()};

        String[] selectionArguments = new String[]{"1"};

        //TODO CAPIRE QUESTA COSA DELLE QUERY
        Log.d("MoviesProvider", uri.toString());

//        TODO SISTEMARE QUI C'È QUALCOSA CHE NON VA: MATCHER E NEMMENO VALORI
        switch (sUriMatcher.match(uri)){

            case CODE_BEST: {
                String valueEntry = uri.getLastPathSegment();

                cursor = mDBHelper.getReadableDatabase().query(
                        MovieEntry.NAME_TABLE,
                        projection,
                        valueEntry + " >= ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );

                Log.e("FFFFFFFF", uri + " " + valueEntry+ " " +String.valueOf(cursor.getCount()));

            }
            break;

            case CODE_FAVOURITE: {

                cursor = mDBHelper.getReadableDatabase().query(
                        MovieEntry.NAME_TABLE,
                        projection,
                        MovieEntry.IS_FAVOURITE + " >= ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                //TODO SOLO QUI FUNZIONA NON NEGLI ALTRI MATCH
                Log.e("FFFFFFFF", String.valueOf(cursor.getCount()));

            }
            break;

            default:
                throw new UnsupportedOperationException(getContext().getString(
                        R.string.error_invalid_uri,
                        uri
                ));
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;

        int match = sUriMatcher.match(uri);

        if(match == CODE_BEST || match == CODE_FAVOURITE) {
            numRowsDeleted = mDBHelper.getWritableDatabase().delete(
                    MovieEntry.NAME_TABLE,
                    selection,
                    selectionArgs
            );
        }else{
            //TODO hardcoded here, must to fix this
            throw new UnsupportedOperationException(getContext().getString(
                    R.string.error_invalid_uri,
                    uri
            ));
        }

        if(numRowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return numRowsDeleted;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int numRowsUpdated;

        switch (sUriMatcher.match(uri)){
            case CODE_FAVOURITE:
                numRowsUpdated = mDBHelper.getWritableDatabase().update(
                        MovieEntry.NAME_TABLE,
                        values,
                        "_id=" + MovieEntry._ID,
                        null
                );

                break;

            default:
                //TODO hardcoded here, must to fix this
                throw new UnsupportedOperationException(getContext().getString(
                        R.string.error_invalid_uri,
                        uri
                ));
        }

        if(numRowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return numRowsUpdated;
    }

    @Override
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }
}
