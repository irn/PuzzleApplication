package com.softevol.appsystemimpl.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.data.PuzzlesTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: antony
 * Date: 1/27/13
 * Time: 3:06 PM
 */
public class PuzzlesAdapter extends SimpleCursorAdapter {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat();

    public PuzzlesAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, 0);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndex(PuzzlesTable.COLUMN_ID));

        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        TextView lastPlayedTextView = (TextView) view.findViewById(R.id.last_played_text_view);
        TextView isAutoSolveTextView = (TextView) view.findViewById(R.id.autosolve_text_view);

        String name = cursor.getString(cursor.getColumnIndex(PuzzlesTable.COLUMN_PUZZLE_NAME));
        long lastPlayed = cursor.getLong(cursor.getColumnIndex(PuzzlesTable.COLUMN_USER_LAST_PLAYED));
        boolean isAutoSolve = cursor.getInt(cursor.getColumnIndex(PuzzlesTable.COLUMN_PUZZLE_AUTO_SOLVE)) > 0;

        nameTextView.setText(name);
        if (lastPlayed > 0) {
            lastPlayedTextView.setText(DATE_FORMATTER.format(new Date(lastPlayed)));
        } else {
            lastPlayedTextView.setText("Never");
        }
        isAutoSolveTextView.setText(isAutoSolve ? "Yes" : "No");

        if (id == mActiveId) {
            nameTextView.setBackgroundColor(context.getResources().getColor(R.color.puzzle_name_selected));
        } else {
            nameTextView.setBackgroundColor(context.getResources().getColor(R.color.puzzle_name));
        }

        nameTextView.setGravity(Gravity.CENTER);

        nameTextView.setTag(id);
    }

    public long getActiveId() {
        return mActiveId;
    }

    public void setActiveId(long activeId) {
        if (activeId == mActiveId) {
            mActiveId = -1;
        } else {
            mActiveId = activeId;
        }
        notifyDataSetInvalidated();
    }

    private long mActiveId = -1;
}
