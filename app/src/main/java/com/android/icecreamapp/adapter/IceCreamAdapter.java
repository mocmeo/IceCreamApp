package com.android.icecreamapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.icecreamapp.model.IceCream;
import com.android.icecreamapp.R;
import java.util.List;

public class IceCreamAdapter extends ArrayAdapter<IceCream> {

    private Context context;
    private int layout;
    private List<IceCream> arrIceCream;



    public IceCreamAdapter(@NonNull Context context, int resource, @NonNull List<IceCream> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.arrIceCream = objects; this.context = context;
        this.layout = resource;
        this.arrIceCream = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(layout, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final IceCream s = arrIceCream.get(position);
        if (getCount() > 0) {
            holder.txtName.setText(s.getName());
            holder.txtDescription.setText(s.getDescription());
        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtDescription;

    }
}