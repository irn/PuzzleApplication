package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.fragment.TabsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:08 PM
 */
public class NewsActivity extends BaseActivity implements TabsFragment.OnTabChangedListener {

    private static final int TAB_HEADLINES = 1;
    private static final int TAB_WORD = 2;
    private static final int TAB_UK = 3;
    private static final int TAB_SPORT = 4;

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mTabsContent.add(findViewById(R.id.news_tab_content_headlines));
        mTabsContent.add(findViewById(R.id.news_tab_content_word));
        mTabsContent.add(findViewById(R.id.news_tab_content_uk));
        mTabsContent.add(findViewById(R.id.news_tab_content_sport));
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();
        tabsFragment.setOnTabChangedListener(this);

        TabsFragment.TabView tabHeadlines = new TabsFragment.TabView(this, "Headlines");
        tabHeadlines.setId(TAB_HEADLINES);
        tabsFragment.addTab(tabHeadlines);

        TabsFragment.TabView tabWord = new TabsFragment.TabView(this, "Word");
        tabWord.setId(TAB_WORD);
        tabsFragment.addTab(tabWord);

        TabsFragment.TabView tabUK = new TabsFragment.TabView(this, "UK");
        tabUK.setId(TAB_UK);
        tabsFragment.addTab(tabUK);

        TabsFragment.TabView tabSport = new TabsFragment.TabView(this, "Sport");
        tabSport.setId(TAB_SPORT);
        tabsFragment.addTab(tabSport);
    }

    @Override
    public void onTabChanged(TabsFragment.TabView prevActiveTab, TabsFragment.TabView newActiveTab) {
        mTabsContent.get(prevActiveTab.getId() - 1).setVisibility(View.GONE);
        mTabsContent.get(newActiveTab.getId() - 1).setVisibility(View.VISIBLE);
    }

    private List<View> mTabsContent = new ArrayList<View>();
}
