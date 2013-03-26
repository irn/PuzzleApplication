package com.softevol.appsystemimpl.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * Date: 2/3/13
 * Time: 2:07 PM
 */
public class HighScoreAdapter extends SimpleCursorAdapter {
    public HighScoreAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, 0);
    }
}
