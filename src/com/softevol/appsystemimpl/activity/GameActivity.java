package com.softevol.appsystemimpl.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.data.PuzzlesContentProvider;
import com.softevol.appsystemimpl.data.PuzzlesTable;
import com.softevol.appsystemimpl.view.GameView;

import java.util.ArrayList;
import java.util.List;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:11 PM
 */
public class GameActivity extends BaseActivity implements GameView.OnSizeKnownListener, GameView.OnPuzzleSolvedListener {
    private static final String TAG = GameView.class.getSimpleName();

    private static final String EXTRA_PUZZLE_ID = "extra_puzzle_id";

    public static void launch(Context context, long puzzleId) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_PUZZLE_ID, puzzleId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mProgressBar = (ProgressBar) findViewById(R.id.next_puzzle_progress_bar);
        mGameView = (GameView) findViewById(R.id.game_view);

        mTopSpaceHolder = findViewById(R.id.top_space_holder);
        mBottomSpaceHolder = findViewById(R.id.bottom_space_holder);

        mGameView.setOnSizeKnownListener(this);
        mGameView.setOnPuzzleSolvedListener(this);

        mPuzzleId = getIntent().getLongExtra(EXTRA_PUZZLE_ID, -1);
        Cursor cursor = getContentResolver().query(PuzzlesContentProvider.CONTENT_URI,
                new String[]{PuzzlesTable.COLUMN_USER_COMPLETED}, PuzzlesTable.COLUMN_PUZZLE_ID + " = " + mPuzzleId, null, null);
        cursor.moveToFirst();

        mPuzzleSolved = cursor.getInt(cursor.getColumnIndex(PuzzlesTable.COLUMN_USER_COMPLETED)) > 0;
        cursor.close();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        View menuView = getLayoutInflater().inflate(R.layout.navigation_custom_menu_game, null);
        menuView.findViewById(R.id.next_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(
                        PuzzlesContentProvider.CONTENT_URI,
                        new String[] {PuzzlesTable.COLUMN_PUZZLE_ID, PuzzlesTable.COLUMN_USER_COMPLETED},
                        null,
                        null,
                        null
                        );

                List<Long> puzzleIds = new ArrayList<Long>();
                List<Boolean> puzzleSolved = new ArrayList<Boolean>();

                while (cursor.moveToNext()) {
                    puzzleIds.add(cursor.getLong(cursor.getColumnIndex(PuzzlesTable.COLUMN_PUZZLE_ID)));
                    puzzleSolved.add(cursor.getInt(cursor.getColumnIndex(PuzzlesTable.COLUMN_USER_COMPLETED)) > 0);
                }

                cursor.close();

                int i = puzzleIds.indexOf(mPuzzleId);
                if (i + 1 < puzzleIds.size()) {
                    mPuzzleId = puzzleIds.get(i + 1);
                    mPuzzleSolved = puzzleSolved.get(i + 1);
                } else {
                    mPuzzleId = puzzleIds.get(0);
                    mPuzzleSolved = puzzleSolved.get(0);
                }

                mNextPuzzleLoader = new NextPuzzleLoader();
                mNextPuzzleLoader.execute(mPuzzleId);
            }
        });
        getNavigationFragment().setCustomMenuView(menuView);
    }

    @Override
    public void onSizeKnown() {
        mNextPuzzleLoader = new NextPuzzleLoader();
        mNextPuzzleLoader.execute(mPuzzleId);
    }

    @Override
    public void onPuzzleSolved() {
        Toast.makeText(this, "Solved", Toast.LENGTH_LONG).show();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PuzzlesTable.COLUMN_USER_COMPLETED, 1);
        getContentResolver().update(PuzzlesContentProvider.CONTENT_URI, contentValues,
                PuzzlesTable.COLUMN_PUZZLE_ID + " = " + mPuzzleId, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNextPuzzleLoader != null) {
            mNextPuzzleLoader.cancel(true);
        }
    }

    private class NextPuzzleLoader extends AsyncTask<Long, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

            mTopSpaceHolder.setVisibility(View.VISIBLE);
            mBottomSpaceHolder.setVisibility(View.VISIBLE);

            mGameView.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            try {
                mGameView.next(params[0], mPuzzleSolved);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                mProgressBar.setVisibility(View.GONE);

                mTopSpaceHolder.setVisibility(View.GONE);
                mBottomSpaceHolder.setVisibility(View.GONE);

                mGameView.setVisibility(View.VISIBLE);

                mNextPuzzleLoader = null;
            } else {
                Toast.makeText(GameActivity.this, "Unknown error...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private NextPuzzleLoader mNextPuzzleLoader;
    private ProgressBar mProgressBar;
    private GameView mGameView;

    private View mTopSpaceHolder;
    private View mBottomSpaceHolder;

    private long mPuzzleId;
    private boolean mPuzzleSolved;
}
