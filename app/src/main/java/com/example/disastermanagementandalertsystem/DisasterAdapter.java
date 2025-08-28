package com.example.disastermanagementandalertsystem;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DisasterAdapter extends BaseAdapter {

    private Context context;
    private List<DisasterModel> disasterList;
    private LayoutInflater inflater;

    public DisasterAdapter(Context context, List<DisasterModel> disasterList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.disasterList = disasterList;
    }

    @Override
    public int getCount() {
        return disasterList.size();
    }

    @Override
    public Object getItem(int position) {
        return disasterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView disasterName, disasterDetails, date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alert_cards, parent, false);
            holder = new ViewHolder();
            holder.disasterName = convertView.findViewById(R.id.disaster);
            holder.disasterDetails = convertView.findViewById(R.id.description);
            holder.date = convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DisasterModel model = disasterList.get(position);
        holder.disasterName.setText(model.getName());
        holder.disasterDetails.setText(model.getDescription());
        holder.date.setText(model.getAlertLevel()); // Or actual date field

        return convertView;
    }
}

