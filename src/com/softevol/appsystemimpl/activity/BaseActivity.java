package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.softevol.appsystemimpl.R;
import com.softevol.appsystemimpl.fragment.MenuAndShareFragment;
import com.softevol.appsystemimpl.fragment.NavigationFragment;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:03 PM
 *
 * <b>Contacts:</b>
 *
 */
public abstract class BaseActivity extends FragmentActivity
        implements NavigationFragment.OnNavigationActionsListener, MenuAndShareFragment.OnMenuAndShareActionsListener {

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        mNavigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);
        mNavigationFragment.setOnNavigationActionsListener(this);

        mMenuAndShareFragment = (MenuAndShareFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_menu_and_share);
        mMenuAndShareFragment.setOnMenuAndShareActionsListener(this);
    }

    /**
     * Handle back press, as we have button for navigation
     */
    @Override
    public void onBackPressed() {
        ;
    }

    protected NavigationFragment getNavigationFragment() {
        return mNavigationFragment;
    }

    protected MenuAndShareFragment getMenuAndShareFragment() {
        return mMenuAndShareFragment;
    }

    @Override
    public void handleBack() {
        finish();
    }

    @Override
    public void handleExit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void shareTwitterClick() {
        Toast.makeText(this, "Twitter. In Development...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareFacebookClick() {
        Toast.makeText(this, "Facebook. In Development...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editProfileClick() {
        EditProfileActivity.launch(this);
    }

    private NavigationFragment mNavigationFragment;
    private MenuAndShareFragment mMenuAndShareFragment;
}
