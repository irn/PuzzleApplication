package com.softevol.appsystemimpl.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.adapter.PuzzlesAdapter;
import com.softevol.appsystemimpl.data.PuzzlesContentProvider;
import com.softevol.appsystemimpl.data.PuzzlesTable;
import com.softevol.appsystemimpl.fragment.TabsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:10 PM
 */
public class PuzzlesActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, TabsFragment.OnTabChangedListener {

    private static final int TAB_CREATED = 1;
    private static final int TAB_IMPORTED = 2;
    private static final int TAB_BUY = 3;
    private static final int TAB_ALL = 4;

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, PuzzlesActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzles);

        mTabsContent.add((ListView) findViewById(R.id.created_puzzles_list_view));
        mTabsContent.add((ListView) findViewById(R.id.imported_puzzles_list_view));
        mTabsContent.add((ListView) findViewById(R.id.buy_puzzles_list_view));
        mTabsContent.add((ListView) findViewById(R.id.all_puzzles_list_view));

        fillData();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();
        tabsFragment.setOnTabChangedListener(this);

        TabsFragment.TabView createdTabView = new TabsFragment.TabView(this, "Created");
        createdTabView.setId(TAB_CREATED);
        tabsFragment.addTab(createdTabView);

        TabsFragment.TabView importedTabView = new TabsFragment.TabView(this, "Imported");
        importedTabView.setId(TAB_IMPORTED);
        tabsFragment.addTab(importedTabView);

        TabsFragment.TabView buyTabView = new TabsFragment.TabView(this, "Buy");
        buyTabView.setId(TAB_BUY);
        tabsFragment.addTab(buyTabView);

        TabsFragment.TabView allTabView = new TabsFragment.TabView(this, "All");
        allTabView.setId(TAB_ALL);
        tabsFragment.addTab(allTabView);

        View view = getLayoutInflater().inflate(R.layout.navigation_custom_menu_puzzles, null);
        view.findViewById(R.id.play_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean started = false;
                for (ListView listView : mTabsContent) {
                    long activeId = ((PuzzlesAdapter) listView.getAdapter()).getActiveId();

                    if (activeId > 0) {
                        Cursor cursor = getContentResolver().query(
                                PuzzlesContentProvider.CONTENT_URI,
                                new String[]{PuzzlesTable.COLUMN_PUZZLE_ID},
                                "_id = " + activeId,
                                null,
                                null
                        );
                        cursor.moveToFirst();

                        long puzzleId = cursor.getLong(cursor.getColumnIndex(PuzzlesTable.COLUMN_PUZZLE_ID));

                        GameActivity.launch(PuzzlesActivity.this, puzzleId);
                        started = true;

                        cursor.close();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PuzzlesTable.COLUMN_USER_LAST_PLAYED, System.currentTimeMillis());
                        getContentResolver().update(PuzzlesContentProvider.CONTENT_URI, contentValues, "_id = " + activeId, null);
                    }
                }

                if (!started) {
                    Toast.makeText(PuzzlesActivity.this, "Please select puzzle first", Toast.LENGTH_LONG).show();
                }
            }
        });
        getNavigationFragment().setCustomMenuView(view);
    }

    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{
                PuzzlesTable.COLUMN_PUZZLE_NAME,
                PuzzlesTable.COLUMN_USER_LAST_PLAYED,
                PuzzlesTable.COLUMN_PUZZLE_AUTO_SOLVE
        };
        // Fields on the UI to which we map
        int[] to = new int[]{
                R.id.name_text_view,
                R.id.last_played_text_view,
                R.id.autosolve_text_view
        };

        getSupportLoaderManager().initLoader(TAB_CREATED, null, this);
        getSupportLoaderManager().initLoader(TAB_IMPORTED, null, this);
        getSupportLoaderManager().initLoader(TAB_BUY, null, this);
        getSupportLoaderManager().initLoader(TAB_ALL, null, this);

        for (ListView listView : mTabsContent) {
            listView.setAdapter(new PuzzlesAdapter(this, R.layout.list_view_item_puzzles, null, from, to));
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((PuzzlesAdapter) parent.getAdapter()).setActiveId(id);
                }
            });
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection = {
                PuzzlesTable.COLUMN_ID,
                PuzzlesTable.COLUMN_PUZZLE_NAME,
                PuzzlesTable.COLUMN_USER_LAST_PLAYED,
                PuzzlesTable.COLUMN_PUZZLE_AUTO_SOLVE,
        };

        String selection = null;
        switch (id) {
            case TAB_CREATED:
                selection = "puzzle_source = \"created\"";
                break;
            case TAB_IMPORTED:
                selection = "puzzle_source = \"imported\"";
                break;
            case TAB_BUY:
                selection = "puzzle_source = \"buy\"";
                break;
        }

        CursorLoader cursorLoader = new CursorLoader(this,
                PuzzlesContentProvider.CONTENT_URI, projection, selection, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        ((SimpleCursorAdapter) mTabsContent.get(cursorLoader.getId() - 1).getAdapter()).swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        ((SimpleCursorAdapter) mTabsContent.get(cursorLoader.getId() - 1).getAdapter()).swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        ContentValues values = new ContentValues();
        values.put(PuzzlesTable.COLUMN_PUZZLE_ID, 1);
        values.put(PuzzlesTable.COLUMN_PUZZLE_NAME, "Puzzle name");
        values.put(PuzzlesTable.COLUMN_PUZZLE_SIZE, "COLUMN_PUZZLE_SIZE");
        values.put(PuzzlesTable.COLUMN_PUZZLE_AUTHOR, "COLUMN_PUZZLE_AUTHOR");
        values.put(PuzzlesTable.COLUMN_PUZZLE_CREATION_DATE, 1);

        Random random = new Random();
        String source = "all";
        switch (random.nextInt(3)) {
            case 0:
                source = "created";
                break;
            case 1:
                source = "imported";
                break;
            case 2:
                source = "buy";
                break;
        }

        Toast.makeText(this, "New puzzle created in: " + source, Toast.LENGTH_SHORT).show();

        values.put(PuzzlesTable.COLUMN_PUZZLE_SOURCE, source);
        values.put(PuzzlesTable.COLUMN_PUZZLE_AUTO_SOLVE, 1);
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
        values.put(PuzzlesTable.COLUMN_USER_LAST_PLAYED, System.currentTimeMillis());

        getContentResolver().insert(PuzzlesContentProvider.CONTENT_URI, values);
    }

    @Override
    public void onTabChanged(TabsFragment.TabView prevActiveTab, TabsFragment.TabView newActiveTab) {
        ((PuzzlesAdapter) mTabsContent.get(prevActiveTab.getId() - 1).getAdapter()).setActiveId(-1);
        mTabsContent.get(prevActiveTab.getId() - 1).setVisibility(View.GONE);
        mTabsContent.get(newActiveTab.getId() - 1).setVisibility(View.VISIBLE);
    }

    private List<ListView> mTabsContent = new ArrayList<ListView>();
}
