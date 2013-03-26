package com.softevol.appsystemimpl.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * User: antony
 * Date: 1/26/13
 * Time: 11:46 AM
 */
public class PuzzlesContentProvider extends ContentProvider {
    private static final String TAG = PuzzlesContentProvider.class.getSimpleName();

    // Used for the UriMacher
    private static final int PUZZLES = 10;
    private static final int PUZZLE_ID = 20;

    private static final String AUTHORITY = "com.softevol.appsystemimpl.data";

    private static final String BASE_PATH = "puzzles";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/puzzles";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/puzzle";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PUZZLES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PUZZLE_ID);
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new PuzzlesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(PuzzlesTable.TABLE_PUZZLES);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PUZZLES:
                break;
            case PUZZLE_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(PuzzlesTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        long id;
        switch (uriType) {
            case PUZZLES:
                id = sqlDB.insert(PuzzlesTable.TABLE_PUZZLES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PUZZLES:
                rowsDeleted = sqlDB.delete(PuzzlesTable.TABLE_PUZZLES, selection, selectionArgs);
                break;
            case PUZZLE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PuzzlesTable.TABLE_PUZZLES,
                            PuzzlesTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(PuzzlesTable.TABLE_PUZZLES,
                            PuzzlesTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PUZZLES:
                rowsUpdated = sqlDB.update(
                        PuzzlesTable.TABLE_PUZZLES,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case PUZZLE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(PuzzlesTable.TABLE_PUZZLES, values,
                            PuzzlesTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(PuzzlesTable.TABLE_PUZZLES, values,
                            PuzzlesTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {
                PuzzlesTable.COLUMN_PUZZLE_ID,
                PuzzlesTable.COLUMN_PUZZLE_NAME,
                PuzzlesTable.COLUMN_PUZZLE_SIZE,
                PuzzlesTable.COLUMN_PUZZLE_AUTHOR,
                PuzzlesTable.COLUMN_PUZZLE_CREATION_DATE,
                PuzzlesTable.COLUMN_PUZZLE_SOURCE,
                PuzzlesTable.COLUMN_PUZZLE_AUTO_SOLVE,
                PuzzlesTable.COLUMN_PUZZLE_PRICE,
                PuzzlesTable.COLUMN_TO_WIN_MAX_MOVES,
                PuzzlesTable.COLUMN_TO_WIN_MAX_TIME,
                PuzzlesTable.COLUMN_TO_WIN_MAX_ATTEMPTS,
                PuzzlesTable.COLUMN_WIN_TEXT,
                PuzzlesTable.COLUMN_WIN_CREDITS,
                PuzzlesTable.COLUMN_USER_COMPLETED,
                PuzzlesTable.COLUMN_USER_TURNS,
                PuzzlesTable.COLUMN_USER_ATTEMPTS,
                PuzzlesTable.COLUMN_USER_TIME,
                PuzzlesTable.COLUMN_USER_LAST_PLAYED,
                PuzzlesTable.COLUMN_ID
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }


    private PuzzlesDatabaseHelper mDatabaseHelper;
}
