package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.fragment.TabsFragment;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:09 PM
 */
public class EditProfileActivity extends BaseActivity {

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        getNavigationFragment().setBackButtonVisibility(View.INVISIBLE);

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();

        TabsFragment.TabView createdTabView = new TabsFragment.TabView(this, "Edit");
        createdTabView.setId(1);
        tabsFragment.addTab(createdTabView);
    }

    public void handleCancel(View view) {
        finish();
    }

    public void handleSave(View view) {
        finish();
    }
}
