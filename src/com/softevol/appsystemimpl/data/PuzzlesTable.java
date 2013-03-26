package com.softevol.appsystemimpl.data;

import android.database.sqlite.SQLiteDatabase;

/**
 * User: antony
 * Date: 1/25/13
 * Time: 6:29 PM
 */
public class PuzzlesTable {

    public static final String TABLE_PUZZLES = "puzzles";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PUZZLE_ID = "puzzle_id";
    public static final String COLUMN_PUZZLE_NAME = "puzzle_name";
    public static final String COLUMN_PUZZLE_SIZE = "puzzle_size";
    public static final String COLUMN_PUZZLE_AUTHOR = "puzzle_author";
    public static final String COLUMN_PUZZLE_CREATION_DATE = "puzzle_creation_date";
    public static final String COLUMN_PUZZLE_SOURCE = "puzzle_source";
    public static final String COLUMN_PUZZLE_AUTO_SOLVE = "puzzle_auto_solve";
    public static final String COLUMN_PUZZLE_PRICE = "puzzle_price";
    public static final String COLUMN_TO_WIN_MAX_MOVES = "to_win_max_moves";
    public static final String COLUMN_TO_WIN_MAX_TIME = "to_win_max_time";
    public static final String COLUMN_TO_WIN_MAX_ATTEMPTS = "to_win_max_attempts";
    public static final String COLUMN_WIN_TEXT = "win_text";
    public static final String COLUMN_WIN_CREDITS = "win_credits";
    public static final String COLUMN_USER_COMPLETED = "user_completed";
    public static final String COLUMN_USER_TURNS = "user_turns";
    public static final String COLUMN_USER_ATTEMPTS = "user_attempts";
    public static final String COLUMN_USER_TIME = "user_time";
    public static final String COLUMN_USER_LAST_PLAYED = "user_last_played";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PUZZLES
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PUZZLE_ID + " integer,"
            + COLUMN_PUZZLE_NAME + " text not null,"
            + COLUMN_PUZZLE_SIZE + " text not null,"
            + COLUMN_PUZZLE_AUTHOR + " text not null,"
            + COLUMN_PUZZLE_CREATION_DATE + " integer,"
            + COLUMN_PUZZLE_SOURCE + " text not null,"
            + COLUMN_PUZZLE_AUTO_SOLVE + " integer,"
            + COLUMN_PUZZLE_PRICE + " integer,"
            + COLUMN_TO_WIN_MAX_MOVES + " integer,"
            + COLUMN_TO_WIN_MAX_TIME + " integer,"
            + COLUMN_TO_WIN_MAX_ATTEMPTS + " integer,"
            + COLUMN_WIN_TEXT + " text,"
            + COLUMN_WIN_CREDITS + " integer,"
            + COLUMN_USER_COMPLETED + " integer,"
            + COLUMN_USER_TURNS + " integer,"
            + COLUMN_USER_ATTEMPTS + " integer,"
            + COLUMN_USER_LAST_PLAYED + " integer,"
            + COLUMN_USER_TIME
            + " integer"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ;
    }
}
