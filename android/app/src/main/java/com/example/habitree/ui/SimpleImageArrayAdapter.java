package com.example.habitree.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.habitree.R;

public class SimpleImageArrayAdapter extends ArrayAdapter<Integer> {
    private Integer[] colors;

    public SimpleImageArrayAdapter(Context context, Integer[] colors) {
        super(context, android.R.layout.simple_spinner_item, colors);
        this.colors = colors;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(new ColorDrawable(colors[position]));
        float imageSize =  imageView.getResources().getDimensionPixelSize(R.dimen.medium);
        float paddingSize =  imageView.getResources().getDimensionPixelSize(R.dimen.small);

        imageView.setLayoutParams(new AbsListView.LayoutParams((int) imageSize, (int) imageSize));
        imageView.setPadding((int) paddingSize,(int) paddingSize,(int) paddingSize,(int) paddingSize);
        return imageView;
    }
}