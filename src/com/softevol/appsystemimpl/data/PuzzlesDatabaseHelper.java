package com.softevol.appsystemimpl.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.softevol.appsystemimpl.util.FileSystem;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

/**
 * User: antony
 * Date: 1/25/13
 * Time: 6:39 PM
 */
public class PuzzlesDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = PuzzlesDatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "puzzles.db";
    private static final int DATABASE_VERSION = 1;

    public PuzzlesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PuzzlesTable.onCreate(db);

        new Thread(new Runnable() {
            @Override
            public void run() {
                setUpData();
            }
        }).start();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PuzzlesTable.onUpgrade(db, oldVersion, newVersion);
    }

    private void setUpData() {
        try {
            AssetManager assetManager = mContext.getAssets();
            SQLiteDatabase database = getWritableDatabase();
            int puzzleId = 0;
            for (String filename : assetManager.list("puzzles")) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open("puzzles/" + filename);
                    out = new FileOutputStream(new File(FileSystem.PROJECT_DIRECTORY, String.valueOf(puzzleId) + ".jpg"));
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();

                    String[] fvalues = filename.substring(0, filename.length() - 4).split("-");
                    Log.d(TAG, "name: " + fvalues[0]);
                    Log.d(TAG, "size: " + fvalues[1]);

                    ContentValues values = new ContentValues();
                    values.put(PuzzlesTable.COLUMN_PUZZLE_ID, puzzleId++);
                    values.put(PuzzlesTable.COLUMN_PUZZLE_NAME, fvalues[0]);
                    values.put(PuzzlesTable.COLUMN_PUZZLE_SIZE, fvalues[1]);
                    values.put(PuzzlesTable.COLUMN_PUZZLE_AUTHOR, "unknown");
                    values.put(PuzzlesTable.COLUMN_PUZZLE_CREATION_DATE, System.currentTimeMillis());
                    values.put(PuzzlesTable.COLUMN_PUZZLE_SOURCE, "created");
                    values.put(PuzzlesTable.COLUMN_PUZZLE_AUTO_SOLVE, 0);
                    values.put(PuzzlesTable.COLUMN_PUZZLE_PRICE, 0);
                    values.put(PuzzlesTable.COLUMN_TO_WIN_MAX_MOVES, -1);
                    values.put(PuzzlesTable.COLUMN_TO_WIN_MAX_TIME, -1);
                    values.put(PuzzlesTable.COLUMN_TO_WIN_MAX_ATTEMPTS, -1);
                    values.put(PuzzlesTable.COLUMN_WIN_TEXT, "");
                    values.put(PuzzlesTable.COLUMN_WIN_CREDITS, 0);
                    values.put(PuzzlesTable.COLUMN_USER_COMPLETED, 0);
                    values.put(PuzzlesTable.COLUMN_USER_TURNS, 0);
                    values.put(PuzzlesTable.COLUMN_USER_ATTEMPTS, 0);
                    values.put(PuzzlesTable.COLUMN_USER_TIME, 0);
                    values.put(PuzzlesTable.COLUMN_USER_LAST_PLAYED, -1);


                    long insertedId = database.insert(PuzzlesTable.TABLE_PUZZLES, null, values);
                    Log.d(TAG, "insertedId: " + insertedId);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to copy asset file: " + filename, e);
                }
            }
            database.close();

            Log.d(TAG, "finish!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR", e);
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private Context mContext;
}
