package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.fragment.TabsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TabsFragment.OnTabChangedListener {

    private static final int TAB_MAIN = 1;
    private static final int TAB_FRIENDS = 2;
    private static final int TAB_MESSAGES = 3;
    private static final int TAB_SEARCH = 4;

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabsContent.add(findViewById(R.id.main_tab_content_main));
        mTabsContent.add(findViewById(R.id.main_tab_content_friends));
        mTabsContent.add(findViewById(R.id.main_tab_content_messages));
        mTabsContent.add(findViewById(R.id.main_tab_content_search));
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        getNavigationFragment().setBackButtonVisibility(View.INVISIBLE);
        getMenuAndShareFragment().setEditProfileViewVisibility(View.VISIBLE);

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();
        tabsFragment.setOnTabChangedListener(this);

        TabsFragment.TabView tabHeadlines = new TabsFragment.TabView(this, "Main");
        tabHeadlines.setId(TAB_MAIN);
        tabsFragment.addTab(tabHeadlines);

        TabsFragment.TabView tabWord = new TabsFragment.TabView(this, "Friends");
        tabWord.setId(TAB_FRIENDS);
        tabsFragment.addTab(tabWord);

        TabsFragment.TabView tabUK = new TabsFragment.TabView(this, "Messages");
        tabUK.setId(TAB_MESSAGES);
        tabsFragment.addTab(tabUK);

        TabsFragment.TabView tabSport = new TabsFragment.TabView(this, "Search");
        tabSport.setId(TAB_SEARCH);
        tabsFragment.addTab(tabSport);
    }

    public void handleOpenPuzzles(View view) {
        PuzzlesActivity.launch(this);
    }

    public void handleCreatePuzzle(View view) {
        Toast.makeText(this, "Create puzzle. In development...", Toast.LENGTH_SHORT).show();
    }

    public void handleSharePuzzle(View view) {
        Toast.makeText(this, "Share puzzle. In development...", Toast.LENGTH_SHORT).show();
    }

    public void handleDownloadPuzzles(View view) {
        Toast.makeText(this, "Download puzzles. In development...", Toast.LENGTH_SHORT).show();
    }

    public void handleBuyPuzzles(View view) {
        Toast.makeText(this, "Buy puzzles. In development...", Toast.LENGTH_SHORT).show();
    }

    public void handleLogout(View view) {
        Toast.makeText(this, "Logout. In development...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabChanged(TabsFragment.TabView prevActiveTab, TabsFragment.TabView newActiveTab) {
        mTabsContent.get(prevActiveTab.getId() - 1).setVisibility(View.GONE);
        mTabsContent.get(newActiveTab.getId() - 1).setVisibility(View.VISIBLE);
    }

    private List<View> mTabsContent = new ArrayList<View>();
}
