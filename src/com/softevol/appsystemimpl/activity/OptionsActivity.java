package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.fragment.TabsFragment;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:08 PM
 */
public class OptionsActivity extends BaseActivity {

    private static final int TAB_OPTIONS = 0;

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, OptionsActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        TabsFragment tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
        tabsFragment.clearTabs();

        TabsFragment.TabView createdTabView = new TabsFragment.TabView(this, "Options");
        createdTabView.setId(TAB_OPTIONS);
        tabsFragment.addTab(createdTabView);

        View view = getLayoutInflater().inflate(R.layout.navigation_custom_menu_options, null);
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OptionsActivity.this, "Save click", Toast.LENGTH_SHORT).show();
            }
        });
        getNavigationFragment().setCustomMenuView(view);
    }
}
