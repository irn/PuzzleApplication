package com.softevol.appsystemimpl.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.data.PuzzlesContentProvider;
import com.softevol.appsystemimpl.data.PuzzlesTable;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:07 PM
 */
public class HomeActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        getNavigationFragment().setBackButtonVisibility(View.INVISIBLE);
    }

    public void handleOpenMain(View view) {
        SelectProfileActivity.launch(this);
    }

    public void handleOpenOptions(View view) {
        OptionsActivity.launch(this);
    }

    public void handleOpenScores(View view) {
        HighScoreActivity.launch(this);
    }

    public void handleOpenNews(View view) {
        NewsActivity.launch(this);
    }

    public void handleOpenAbout(View view) {
        AboutActivity.launch(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection = { PuzzlesTable.COLUMN_ID, };

        CursorLoader cursorLoader = new CursorLoader(this,
                PuzzlesContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        ;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        ;
    }

}
