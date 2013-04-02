package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.fragment.TabsFragment;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:08 PM
 */
public class AboutActivity extends BaseActivity implements TabsFragment.OnTabChangedListener {

    private static final int TAB_GAME_INFO = 1;
    private static final int TAB_CREDITS = 2;

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mTabGameInfoView = findViewById(R.id.about_game_info_root_layout);
        mTabCreditsView = (WebView) findViewById(R.id.about_game_credits_layout);
        mTabCreditsView.loadUrl("file:///android_asset/aboutus.html");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();
        tabsFragment.setOnTabChangedListener(this);

        TabsFragment.TabView createdTabView = new TabsFragment.TabView(this, getString(R.string.tab_game_info));
        createdTabView.setId(TAB_GAME_INFO);
        tabsFragment.addTab(createdTabView);

        TabsFragment.TabView importedTabView = new TabsFragment.TabView(this, getString(R.string.tab_credits));
        importedTabView.setId(TAB_CREDITS);
        tabsFragment.addTab(importedTabView);
    }

    @Override
    public void onTabChanged(TabsFragment.TabView prevActiveTab, TabsFragment.TabView newActiveTab) {
        if (newActiveTab.getId() == TAB_CREDITS) {
            mTabGameInfoView.setVisibility(View.GONE);
            mTabCreditsView.setVisibility(View.VISIBLE);
        } else {
            mTabGameInfoView.setVisibility(View.VISIBLE);
            mTabCreditsView.setVisibility(View.GONE);
        }
    }

    private View mTabGameInfoView;
    private WebView mTabCreditsView;
}
