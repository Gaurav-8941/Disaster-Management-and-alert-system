package com.example.disastermanagementandalertsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter<ProfileItem> {

    public ProfileAdapter(Context context, List<ProfileItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfileItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_profile, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.profile_icon);
        TextView title = convertView.findViewById(R.id.profile_title);

        icon.setImageResource(item.getIconResId());
        title.setText(item.getTitle());

        return convertView;
    }
}
