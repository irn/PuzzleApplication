package com.softevol.appsystemimpl.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.softevol.appsystemimpl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: antony
 * Date: 1/26/13
 * Time: 2:28 PM
 */
public class TabsFragment extends Fragment implements View.OnClickListener {

    public static interface OnTabChangedListener {
        public void onTabChanged(TabView prevActiveTab, TabView newActiveTab);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View v) {
        TabView tabView = (TabView) v;

        if (!tabView.equals(mActive)) {

            tabView.setActive(true);
            mActive.setActive(false);

            if (mOnTabChangedListener != null) {
                mOnTabChangedListener.onTabChanged(mActive, tabView);
            }

            mActive = tabView;
        }
    }

    public void addTab(TabView tab) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (tab.getId() == mTabs.get(i).getId()) {
                mTabs.remove(i);
            }
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.tab_width), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        if (mTabs.size() > 0) {
            layoutParams.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.tabs_margin),
                    getResources().getDimensionPixelSize(R.dimen.tabs_margin),
                    0, 0);
            layoutParams.addRule(RelativeLayout.RIGHT_OF, mTabs.get(mTabs.size() - 1).getId());
        } else {
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.tabs_margin), 0, 0);
        }

        if (mTabs.size() == 0) {
            tab.setLeft(true);
            tab.setActive(true);
            mActive = tab;
        }

        tab.setOnClickListener(this);
        mTabs.add(tab);

        ((RelativeLayout) getView().findViewById(R.id.root_view)).addView(tab, layoutParams);
    }

    public void setOnTabChangedListener(OnTabChangedListener onTabChangedListener) {
        mOnTabChangedListener = onTabChangedListener;
    }

    public static class TabView extends Button {
        public TabView(Context context, String text) {
            super(context);

            setGravity(Gravity.CENTER);
            setTextColor(getResources().getColor(R.color.main_blue));
            setTextSize(getResources().getDimension(R.dimen.tabs_text_size));
            setText(text);

            updateBackground();
        }

        public void setActive(boolean active) {
            mmIsActive = active;

            updateBackground();
        }

        public void setLeft(boolean left) {
            mmIsLeft = left;

            updateBackground();
        }

        private void updateBackground() {
            if (mmIsLeft) {
                if (mmIsActive) {
                    setBackgroundResource(R.drawable.tab_left_active);
                } else {
                    setBackgroundResource(R.drawable.tab_left_inactive);
                }
            } else {
                if (mmIsActive) {
                    setBackgroundResource(R.drawable.tab_middle_active);
                } else {
                    setBackgroundResource(R.drawable.tab_middle_inactive);
                }
            }
        }

        public boolean isActive() {
            return mmIsActive;
        }

        private boolean mmIsActive;
        private boolean mmIsLeft;
    }

    public void clearTabs() {
        ((RelativeLayout) getView().findViewById(R.id.root_view)).removeViews(1, mTabs.size());
        mTabs.clear();
    }

    private OnTabChangedListener mOnTabChangedListener;
    private TabView mActive;
    private List<TabView> mTabs = new ArrayList<TabView>();
}
