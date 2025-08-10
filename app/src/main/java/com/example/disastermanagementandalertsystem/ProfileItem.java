package com.example.disastermanagementandalertsystem;
public class ProfileItem {
    int iconResId;
    String title;

    public ProfileItem(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }
}
