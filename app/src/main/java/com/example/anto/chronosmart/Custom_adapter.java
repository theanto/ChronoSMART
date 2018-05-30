package com.example.anto.chronosmart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom_adapter extends ArrayAdapter<Weather_condition> {

    ArrayList<Weather_condition> wheater_conditions, tempWeather_condition, suggestions;

    public Custom_adapter(Context context, ArrayList<Weather_condition> objects) {
        super(context, R.layout.dropdown_row, R.id.tvWeather_condition, objects);
        this.wheater_conditions = objects;
        this.tempWeather_condition = new ArrayList<Weather_condition>(objects);
        this.suggestions = new ArrayList<Weather_condition>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, null);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        Weather_condition wc = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_row, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_row, parent, false);
        }
        TextView txtWeather_condition = (TextView) convertView.findViewById(R.id.tvWeather_condition);
        ImageView ivWeather_conditionImage = (ImageView) convertView.findViewById(R.id.ivWeather_conditionImage);
        if (txtWeather_condition != null)
            txtWeather_condition.setText(wc.getDescription());


        if (ivWeather_conditionImage != null)
            ivWeather_conditionImage.setImageResource(wc.getPicture());

        // Now assign alternate color for rows
        if (position % 2 == 0)
            convertView.setBackgroundColor(getContext().getColor(R.color.odd));
        else
            convertView.setBackgroundColor(getContext().getColor(R.color.even));
        ivWeather_conditionImage.setImageResource(wc.getPicture());

        return convertView;
    }

}
