package com.academiamoviles.tracklogcopilotommg.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fernando-PC on 06/02/2017.
 */

public class UsoAppAdapter extends RecyclerView.Adapter<UsoAppAdapter.ViewHolder> {

    private List<String> list;

    public UsoAppAdapter(List<String> list) {
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position==list.size()){
            holder.txtfecha.setText("TOTAL DE MINUTOS EN EL APP: "+10*list.size() + " minutos");
        } else {
            final String item = list.get(position);
            holder.txtfecha.setText(item);
        }

    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtfecha;

        public ViewHolder(View itemView) {
            super(itemView);
            txtfecha = (TextView) itemView;
        }
    }


}
