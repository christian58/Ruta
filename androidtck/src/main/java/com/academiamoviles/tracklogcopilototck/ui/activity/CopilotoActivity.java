package com.academiamoviles.tracklogcopilototck.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.academiamoviles.tracklogcopilototck.R;

/**
 * Created by Android on 06/02/2017.
 */

public class CopilotoActivity extends BaseActivity {

    public static final String COD_RUTA = "cod_ruta";
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copiloto_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState != null)
        	return;
        new Thread(new Runnable() {
			@Override
			public void run() {
		        playSound(R.raw.ruta_inicio);
			}
		}).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //super.onBackPressed();
                showAlert();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Mensaje");
        alertDialog.setMessage("¿Seguro que desea desactivar el copiloto?, se generará un reporte");
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	new Thread(new Runnable() {
					@Override
					public void run() {
		            	playSound(R.raw.ruta_fin);
					}
				}).start();
                Intent reportIntent = new Intent(getApplicationContext(), ReportsActivity.class);
                startActivity(reportIntent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        showAlert();
        //super.onBackPressed();
    }

    private void playSound(int resId) {
    	if (mp != null && mp.isPlaying()) {
    		mp.stop();
    		mp.release();
    	}
    	mp = MediaPlayer.create(this, resId);
    	mp.start();
    	while (mp.isPlaying()) {}
    	mp.release();
    	mp = null;
    }
}
