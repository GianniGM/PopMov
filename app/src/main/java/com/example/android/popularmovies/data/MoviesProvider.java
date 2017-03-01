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
    public static final int CODE_TOP_RATED = 100;
    public static final int CODE_MOST_POPULAR = 101;
    public static final int CODE_FAVOURITE = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int IS_TRUE = 1;

    private MoviesDBHelper mDBHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = CONTENT_AUTHORITY;
        final String path = PATH_MOVIES;

        uriMatcher.addURI(authority, path ,CODE_TOP_RATED);
        uriMatcher.addURI(authority, path + "/#", CODE_MOST_POPULAR);
        uriMatcher.addURI(authority, path + "/#", CODE_FAVOURITE);

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

            case CODE_MOST_POPULAR: {
                //bulk insertions are safe
                db.beginTransaction();

                int insertedRow = setFlagAndInsert(db, values, MovieEntry.IS_MOST_POPULAR);

                if (insertedRow > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return insertedRow;
            }

            case CODE_TOP_RATED: {
                //bulk insertions are safe
                db.beginTransaction();

                int insertedRow = setFlagAndInsert(db, values, MovieEntry.IS_TOP_RATED);

                if (insertedRow > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return insertedRow;
            }


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

        switch (sUriMatcher.match(uri)){
            case CODE_TOP_RATED: {
                cursor = mDBHelper.getReadableDatabase().query(
                        MovieEntry.NAME_TABLE,
                        projection,
                        MovieEntry.IS_TOP_RATED + " >= ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );

            }
            break;

            case CODE_MOST_POPULAR: {
                cursor = mDBHelper.getReadableDatabase().query(
                        MovieEntry.NAME_TABLE,
                        projection,
                        MovieEntry.IS_MOST_POPULAR + " >= ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );


            }
            break;

            case CODE_FAVOURITE: {
                cursor = mDBHelper.getReadableDatabase().query(
                        MovieEntry.NAME_TABLE,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );

                Log.e("FFFFFFFF", String.valueOf(cursor.getCount()));

            }
            break;

            default:
                //TODO hardcoded here, must to fix this
                Log.e("FFFFFFFF", "ECCEZIONE DEL CAZZO");

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

        if(match == CODE_MOST_POPULAR || match == CODE_TOP_RATED || match == CODE_FAVOURITE) {
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
