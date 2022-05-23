package com.example.reeco;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private final Context m_context;
    private final ArrayList<GridItem> m_array_item;

    public GridAdapter(Context context) {
        this.m_context = context;
        this.m_array_item = new ArrayList<GridItem>();
    }

    @Override
    public int getCount() {
        return this.m_array_item.size();
    }

    @Override
    public Object getItem(int position) {
        return this.m_array_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.grid_item, parent, false);

        ImageButton button = convertView.findViewById(R.id.item_imagebutton);

        TextView textView = convertView.findViewById(R.id.item_textview);
        textView.setText(m_array_item.get(position).getItemString());

        return convertView;
    }

    public void setItem(String strItem) {
        String strGet = strItem;
        this.m_array_item.add(new GridItem(strGet));
    }

    public String getItemString(int position) {
        return this.m_array_item.get(position).getItemString();
    }
}
