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

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GridAdapter extends BaseAdapter {

    private final Context m_context;
    private final ArrayList<GridItem> m_array_item;

    public GridAdapter(Context context) {
        this.m_context = context;
        this.m_array_item = new ArrayList<>();
    }

    @SuppressWarnings("unused")
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

        String positionName = m_array_item.get(position).getItemString();

        AppDatabase database = AppDatabase.getInstance(m_context);

        Observable<String> nameObservable = Observable.just(positionName);

        //noinspection ResultOfMethodCallIgnored
        nameObservable.subscribeOn(Schedulers.io()).subscribe(name -> {
            Server server = database.serverDao().findServerByName(name);

            button.setOnClickListener(view -> {
                Intent intent = new Intent(m_context, CodeWriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ip", server.getIp());
                intent.putExtra("port", server.getPort());
                intent.putExtra("user", server.getUser());
                intent.putExtra("password", server.getPassword());
                intent.putExtra("compiler", server.getCompiler());

                m_context.startActivity(intent);
            });
        }, Throwable::printStackTrace);

        button.setOnLongClickListener(view -> false);

        return convertView;
    }

    @SuppressWarnings("unused")
    public void setItem(String strItem) {
        this.m_array_item.add(new GridItem(strItem));
    }

    public String getItemString(int position) {
        return this.m_array_item.get(position).getItemString();
    }

    public void setAllItems(List<Server> servers) {
        for (Server server : servers) {
            m_array_item.add(new GridItem(server.getName()));
        }
    }

    public void clear() {
        m_array_item.clear();
    }
}
