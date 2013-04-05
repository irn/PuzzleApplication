package com.softevol.appsystemimpl.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.softevol.appsystemimpl.R;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 3:38 PM
 */
public class NavigationFragment extends Fragment implements View.OnClickListener {

    private Activity mActivity;

    public static interface OnNavigationActionsListener {
        public void handleBack();
        public void handleExit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);    //To change body of overridden methods use File | Settings | File Templates.
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
        mBackButton = (ImageButton) rootView.findViewById(R.id.back_button);
        mExitButton = (ImageButton) rootView.findViewById(R.id.navigation_exit_button);

        mCustomMenuViewGroup = (LinearLayout) rootView.findViewById(R.id.menu_layout);

        mBackButton.setOnClickListener(this);
        mExitButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                handleBack();
                break;
            case R.id.navigation_exit_button:
                if (mActivity != null){
                    ExitFragment exitFragment = new ExitFragment();
                    exitFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.theme_close_dialog);
                    exitFragment.show(getActivity().getSupportFragmentManager(), "exit");
                }
//                handleExit();
                break;
        }
    }

    public void handleBack() {
        if (mOnNavigationActionsListener != null) {
            mOnNavigationActionsListener.handleBack();
        }
    }

    public void handleExit() {
        if (mOnNavigationActionsListener != null) {
            mOnNavigationActionsListener.handleExit();
        }
    }

    /**
     * @param visibility View.VISIBLE || View.INVISIBLE
     */
    public void setBackButtonVisibility(int visibility) {
        mBackButton.setVisibility(visibility);
    }

    /**
     * @param visibility View.VISIBLE || View.INVISIBLE
     */
    public void setExitButtonVisibility(int visibility) {
        mExitButton.setVisibility(visibility);
    }

    public void setOnNavigationActionsListener(OnNavigationActionsListener onNavigationActionsListener) {
        mOnNavigationActionsListener = onNavigationActionsListener;
    }

    public void setCustomMenuView(View view) {
        mCustomMenuViewGroup.removeAllViews();
        mCustomMenuViewGroup.addView(view);
    }

    private ImageButton mBackButton;
    private ImageButton mExitButton;

    private OnNavigationActionsListener mOnNavigationActionsListener;

    private ViewGroup mCustomMenuViewGroup;
}
