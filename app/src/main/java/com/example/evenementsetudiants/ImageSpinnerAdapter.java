package com.example.evenementsetudiants;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSpinnerAdapter extends ArrayAdapter<String> {

    Activity context;
    String[] names;
    int[] images;

    public ImageSpinnerAdapter(Activity context, String[] names, int[] images) {

        super(context, R.layout.spinner_item, names);

        this.context = context;
        this.names = names;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_item, null, true);

        ImageView image = row.findViewById(R.id.imgSpinner);
        TextView text = row.findViewById(R.id.txtSpinner);

        image.setImageResource(images[position]);
        text.setText(names[position]);

        return row;
    }
}
