package com.softevol.appsystemimpl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.softevol.appsystemimpl.R;

/**
 * User: antony
 * Date: 1/23/13
 * Time: 7:45 PM
 */
public class UserInfoFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        mAddFreeCreditsButton = (ImageButton) view.findViewById(R.id.buy_free_credits_button);
        mAddPaidCreditsButton = (ImageButton) view.findViewById(R.id.buy_paid_credits_button);
        mAvatarImageView = (ImageView) view.findViewById(R.id.avatar_image_view);
        mCreditsTextView = (TextView) view.findViewById(R.id.credits_text_view);
        mFriendsOnlineTextView = (TextView) view.findViewById(R.id.friends_online_text_view);
        mMessagesTextView = (TextView) view.findViewById(R.id.messages_text_view);
        mUserNameTextView = (TextView) view.findViewById(R.id.user_name_text_view);

        SpannableStringBuilder ssb = new SpannableStringBuilder("username ●");
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_green)),
                9, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mUserNameTextView.setText(ssb, TextView.BufferType.SPANNABLE);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private ImageButton mAddFreeCreditsButton;
    private ImageButton mAddPaidCreditsButton;
    private ImageView mAvatarImageView;
    private TextView mCreditsTextView;
    private TextView mFriendsOnlineTextView;
    private TextView mMessagesTextView;
    private TextView mUserNameTextView;
}
