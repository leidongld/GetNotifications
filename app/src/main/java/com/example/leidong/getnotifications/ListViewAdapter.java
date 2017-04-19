package com.example.leidong.getnotifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by leidong on 2017/4/19.
 */

class ListViewAdapter extends BaseAdapter{
    private Context context;
    private List<Bean> list;
    private LayoutInflater layoutInflator;

    public ListViewAdapter(Context context, List<Bean> list) {
        this.context = context;
        this.list = list;
        layoutInflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflator.inflate(R.layout.notification_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Bean bean = list.get(position);
        viewHolder.title.setText(bean.title);
        viewHolder.text.setText(bean.text);
        return convertView;
    }

    /**
     * 内部类
     */
    public static class ViewHolder{
        public TextView title;
        public TextView text;
    }
}
