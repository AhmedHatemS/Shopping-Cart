package com.example.shoppingcart_ahatem;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter {
    List<ShoppingItem> objects;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<ShoppingItem> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    public View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.spinner_item, parent, false);
        ShoppingItem item = objects.get(position);
        TextView itemName = layout.findViewById(R.id.itemName);
        TextView itemPrice = layout.findViewById(R.id.itemPrice);
        itemName.setText(item.itemName);
        itemPrice.setText(item.itemPrice + " l.e");
        return layout;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }
}
