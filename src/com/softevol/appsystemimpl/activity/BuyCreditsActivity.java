package com.softevol.appsystemimpl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.softevol.appsystemimpl.R;

/**
 * User: antony
 * Date: 1/22/13
 * Time: 5:10 PM
 */
public class BuyCreditsActivity extends BaseActivity {

    public static void launch(Context context) {
        launch(context, null);
    }

    public static void launch(Context context, Bundle params) {
        Intent intent = new Intent(context, BuyCreditsActivity.class);
        intent.replaceExtras(params);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_credits);
    }
}
