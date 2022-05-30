package com.example.reeco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final Context m_context;
    private final ArrayList<GridItem> m_array_item;

    public GridAdapter(Context context) {
        this.m_context = context;
        this.m_array_item = new ArrayList<GridItem>();
    }

    public GridAdapter(Context context, List<Server> servers) {
        this.m_context = context;

        m_array_item = new ArrayList<>();
        for (int i = 0; i < servers.size(); i++) {
            m_array_item.add(new GridItem(servers.get(i).getName()));
        }
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(m_context, CodeWriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                m_context.startActivity(intent);
                return;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

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
