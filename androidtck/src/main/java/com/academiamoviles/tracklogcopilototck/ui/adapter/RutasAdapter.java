package com.academiamoviles.tracklogcopilototck.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;
import com.academiamoviles.tracklogcopilototck.model.Paths;
import com.academiamoviles.tracklogcopilototck.ui.Configuration;
import com.academiamoviles.tracklogcopilototck.ui.activity.LogActivity;
import com.academiamoviles.tracklogcopilototck.ui.activity.ReportsActivity;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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
        final int codRuta = item.nCodRuta;

        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), holder.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_rutas_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.reports:
                                //handle menu1 click
                                System.out.println("*******************RUTAS REPORTS**********************");
                                Configuration.COD_RUTA= codRuta;
                                System.out.println("CODE RUTA: "+Configuration.COD_RUTA);
                                DatabaseHelper mDBHelper = new DatabaseHelper(view.getContext());

                                Paths paths =mDBHelper.getPathsByCodRuta(Configuration.COD_RUTA);

                                //Configuration.IDPLATES_HAS_PATHS = mDBHelper.getLastIdPlatesHasPaths(Configuration.userPlate.getPlates().getIdPlates(),paths.getIdPaths());
                                Configuration.IDPLATES_HAS_PATHS = mDBHelper.getLastIdPlatesHasPaths(paths.getIdPaths());
                                System.out.println("IDPLATES_HAS_PATHS: "+Configuration.IDPLATES_HAS_PATHS);
                                Intent intent = new Intent(view.getContext(), ReportsActivity.class);


                                view.getContext().startActivity(intent);
                                return true;
                            case R.id.log_reports:
                            	Intent i = new Intent(view.getContext(), LogActivity.class);
                                DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                            	Paths path = dbHelper.getPathsByCodRuta(codRuta);
                            	i.putExtra(LogActivity.ID_PATH, path.getIdPaths());
                                view.getContext().startActivity(i);
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

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
        TextView txtTitle, txtDetail, textViewOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDetail = (TextView) itemView.findViewById(R.id.txtDetail);
            textViewOptions = (TextView) itemView.findViewById(R.id.textViewOptions);
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
