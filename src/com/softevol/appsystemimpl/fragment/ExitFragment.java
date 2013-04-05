package com.softevol.appsystemimpl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.softevol.appsystemimpl.R;

/**
 * Created with IntelliJ IDEA.
 * User: android
 * Date: 05.04.13
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class ExitFragment extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.fragment_dialog_close, container, false);
        dialogView.findViewById(R.id.dialogButtonOk).setOnClickListener(this);
        dialogView.findViewById(R.id.dialogButtonCancel).setOnClickListener(this);
        return dialogView;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.dialogButtonOk:
                dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.dialogButtonCancel:
                dismiss();
                break;

        }
    }
}
