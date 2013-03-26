package com.softevol.appsystemimpl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.softevol.appsystemimpl.R;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 3:38 PM
 */
public class MenuAndShareFragment extends Fragment implements View.OnClickListener {

    public static interface OnMenuAndShareActionsListener {
        public void shareTwitterClick();
        public void shareFacebookClick();
        public void editProfileClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_and_share, container, false);
        rootView.findViewById(R.id.share_facebook_button).setOnClickListener(this);
        rootView.findViewById(R.id.share_twitter_button).setOnClickListener(this);

        mEditProfileView = rootView.findViewById(R.id.edit_profile_text_view);
        mEditProfileView.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View v) {
        if (mOnMenuAndShareActionsListener == null) return;

        switch (v.getId()) {
            case R.id.share_facebook_button:
                mOnMenuAndShareActionsListener.shareFacebookClick();
                break;
            case R.id.share_twitter_button:
                mOnMenuAndShareActionsListener.shareTwitterClick();
                break;
            case R.id.edit_profile_text_view:
                mOnMenuAndShareActionsListener.editProfileClick();
                break;
        }
    }

    public void setOnMenuAndShareActionsListener(OnMenuAndShareActionsListener onMenuAndShareActionsListener) {
        mOnMenuAndShareActionsListener = onMenuAndShareActionsListener;
    }

    public void setEditProfileViewVisibility(int visibility) {
        mEditProfileView.setVisibility(visibility);
    }

    private View mEditProfileView;
    private OnMenuAndShareActionsListener mOnMenuAndShareActionsListener;
}
