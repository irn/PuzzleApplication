package com.softevol.appsystemimpl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.softevol.appsystemimpl.model.ScoresModel;

import java.util.List;

/**
 * Date: 2/3/13
 * Time: 2:07 PM
 */
public class HighScoreAdapter extends ArrayAdapter<ScoresModel> {

    private int mResourceId;

    private LayoutInflater mInflater;

    public HighScoreAdapter(Context context, int textViewResourceId, List<ScoresModel> objects) {
        super(context, textViewResourceId, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mInflater.inflate(mResourceId, null);
        }

        return convertView;
    }
}
