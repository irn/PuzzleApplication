package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.adapter.HighScoreAdapter;
import com.softevol.appsystemimpl.fragment.TabsFragment;
import com.softevol.appsystemimpl.model.ScoresModel;

import java.util.ArrayList;
import java.util.List;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:08 PM
 */
public class HighScoreActivity extends BaseActivity implements TabsFragment.OnTabChangedListener {

    private static final int TAB_ALL = 1;
    private static final int TAB_3X3 = 2;
    private static final int TAB_4X4 = 3;
    private static final int TAB_5X5 = 4;

    private ListView mScoreListView;

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, HighScoreActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        mScoreListView = (ListView) findViewById(R.id.listViewScore);
        mTabsContent.add(mScoreListView);
//        mTabsContent.add(findViewById(R.id.scores_tab_content_3x3));
//        mTabsContent.add(findViewById(R.id.scores_tab_content_4x4));
//        mTabsContent.add(findViewById(R.id.scores_tab_content_5x5));


    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();
        tabsFragment.setOnTabChangedListener(this);

        TabsFragment.TabView tabHeadlines = new TabsFragment.TabView(this, "All");
        tabHeadlines.setId(TAB_ALL);
        tabsFragment.addTab(tabHeadlines);

        TabsFragment.TabView tabWord = new TabsFragment.TabView(this, "3x3");
        tabWord.setId(TAB_3X3);
        tabsFragment.addTab(tabWord);

        TabsFragment.TabView tabUK = new TabsFragment.TabView(this, "4x4");
        tabUK.setId(TAB_4X4);
        tabsFragment.addTab(tabUK);

        TabsFragment.TabView tabSport = new TabsFragment.TabView(this, "5x5");
        tabSport.setId(TAB_5X5);
        tabsFragment.addTab(tabSport);
    }

    @Override
    public void onTabChanged(TabsFragment.TabView prevActiveTab, TabsFragment.TabView newActiveTab) {
        ListView view = mTabsContent.get(0);
        List<ScoresModel> scoresModelList = new ArrayList<ScoresModel>();
        scoresModelList.add(new ScoresModel());
        HighScoreAdapter scoreAdapter = new HighScoreAdapter(this, R.layout.item_high_score, scoresModelList);
        view.setAdapter(scoreAdapter);
//        mTabsContent.get(prevActiveTab.getId() - 1).setVisibility(View.GONE);
//        mTabsContent.get(newActiveTab.getId() - 1).setVisibility(View.VISIBLE);
    }

    private List<ListView> mTabsContent = new ArrayList<ListView>();
}
