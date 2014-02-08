package com.matrix.missile.controller.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.model.NavigationDrawerModel;

public class NavBarAdapter extends ArrayAdapter<NavigationDrawerModel> {

    private LayoutInflater mInflater;
    private Context context;
    private static final int NORMAL = 0, SECTION = 1;

    public NavBarAdapter(Context context, int resourceId,
            List<NavigationDrawerModel> items) {
        super(context, resourceId, items);

        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    private class ViewHolder {
        TextView item;
    }

    /**
     * Not Reusing the views, Since we have a section divider in between with
     * different attributes.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        //MyLog.e("MY_LOG","getView");
        ViewHolder holder = null;
        NavigationDrawerModel navModel = getItem(position);
        switch (getItemViewType(position)) {
        case SECTION:
          //  MyLog.e("MY_LOG","section");
            if (convertView == null) {
             //   MyLog.e("MY_LOG","section convertview null");
                convertView = mInflater.inflate(R.layout.list_section,
                        null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            break;

        case NORMAL:
           // MyLog.e("MY_LOG","Normal");
            if (convertView == null) {
                //MyLog.e("MY_LOG","Normal convertview null");
                convertView = mInflater
                        .inflate(R.layout.drawer_list_item, null);
                holder = new ViewHolder();
                holder.item = (TextView) convertView
                        .findViewById(R.id.tvNavBarItem);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.item.setText(navModel.getNavItem());
            break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isSection() ? SECTION : NORMAL;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
