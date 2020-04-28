package com.academiamoviles.tracklogcopilotommg.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.academiamoviles.tracklogcopilotommg.R;
import com.academiamoviles.tracklogcopilotommg.ws.response.Ruta;
import java.util.List;

/**
 * Created by Fernando-PC on 06/02/2017.
 */

public class RutasAdapter extends RecyclerView.Adapter<RutasAdapter.ViewHolder> {

    private OnItemClickListener<Ruta> listener;
    private List<Ruta> list;

    public RutasAdapter(List<Ruta> list) {
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruta_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Ruta item = list.get(position);
        //holder.txtTitle.setText(item.cOriRuta + " - " + item.cDesRuta);
        holder.txtTitle.setText(item.cDescripRuta);
        holder.txtDetail.setText(""+item.nCodRuta);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(OnItemClickListener<Ruta> listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDetail = (TextView) itemView.findViewById(R.id.txtDetail);
        }
    }

    public interface OnItemClickListener<T> {

        /**
         * Metodo que se invoca cuando se ha hecho clic en un Item del RecyclerView.Adapter.
         * <p>
         *
         * @param v        View en el que se hizo Clic.
         * @param position Posición de la view en RecyclerView.Adapter. </p>
         * @param item     objeto seleccionado
         */
        void onItemClick(View v, int position, T item);
    }

}
