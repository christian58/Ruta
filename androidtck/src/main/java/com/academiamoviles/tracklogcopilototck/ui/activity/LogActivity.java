package com.academiamoviles.tracklogcopilototck.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;
import com.academiamoviles.tracklogcopilototck.ui.Configuration;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta_Response;

public class LogActivity extends AppCompatActivity {

	private static final String TAG = LogActivity.class.getSimpleName();
	private List<Report> reportList = new ArrayList<>();
	private ReportAdapter adapter;
	private DbHelper dbHelper;
	public static final String ID_PATH = "ID_PATH";
	private List<Ruta> rutaList = new ArrayList<Ruta>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		dbHelper = new DbHelper(this);
		ListView lv = (ListView) findViewById(R.id.list_log);
		adapter = new ReportAdapter(this, reportList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Report report = (Report) parent.getItemAtPosition(position);
				Configuration.COD_RUTA = report.codRuta;
				Configuration.IDPLATES_HAS_PATHS = report.id;
				Intent i = new Intent(LogActivity.this, ReportsActivity.class);
				startActivity(i);
			}
		});
		Ruta_Response ruta_response = Ruta_Response.readIS(this);
		if (ruta_response != null && !ruta_response.listaRutas.isEmpty())
			rutaList = ruta_response.listaRutas;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		List<Report> list = null;
		Intent i = getIntent();
		if (i.hasExtra(ID_PATH))
			list = dbHelper.getReports(i.getExtras().getInt(ID_PATH));
		else
			list = dbHelper.getReports(null);
		reportList.clear();
		reportList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	class Report {
		public int id;
		public Date date;
		public String placa;
		public int codRuta;

		public Report(int id, Date date, String placa, int codRuta) {
			this.id = id;
			this.date = date;
			this.placa = placa;
			this.codRuta = codRuta;
		}

		@Override
		public String toString() {
			return "Reporte id=" + id + " fecha="
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
		}
	}

	class ReportAdapter extends ArrayAdapter<Report> {

		private int mResource;
		private SimpleDateFormat sdf;

		public ReportAdapter(Context context, List<Report> list) {
			super(context, android.R.layout.simple_list_item_2, list);
			this.mResource = android.R.layout.simple_list_item_2;
			this.sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null)
				view = LayoutInflater.from(getContext()).inflate(mResource,
						parent, false);
			else
				view = convertView;
			Report item = getItem(position);
			String rutaName = "";
			for (Ruta r : rutaList) {
				if (r.nCodRuta == item.codRuta) {
					rutaName = (r.cDescripRuta != null) ? r.cDescripRuta : "";
					break;
				}
			}
			((TextView) view.findViewById(android.R.id.text1)).setText(rutaName 
					+ " (Ruta " + item.codRuta + ")");
			((TextView) view.findViewById(android.R.id.text2)).setText("Placa "
					+ item.placa + ", Fecha " + sdf.format(item.date));
			return view;
		}
	}

	class DbHelper extends DatabaseHelper {
		public DbHelper(Context context) {
			super(context);
		}

		public List<Report> getReports(Integer idPath) {
			Report report = null;
			List<Report> list = new ArrayList<Report>();
			openDatabase();
			String eq = idPath != null ? "WHERE p2.idPaths=" + idPath + " " : "";
			String sql = "SELECT pp.idPlates_has_paths,pp.date,p1.serie,p2.nCodRuta "
					+ "FROM plates_has_paths pp "
					+ "LEFT JOIN plates p1 ON pp.idPlates=p1.idPlates "
					+ "LEFT JOIN paths p2 ON pp.idPaths=p2.idPaths " + eq
					+ "ORDER BY pp.idPlates_has_paths DESC";
			Cursor cur = this.mDatabase.rawQuery(sql, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(cur.getString(1));
				} catch (ParseException e) {
					e.printStackTrace();
					date = null;
				}
				report = new Report(cur.getInt(0), date, cur.getString(2),
						cur.getInt(3));
				list.add(report);
				cur.moveToNext();
			}
			Log.d(TAG, "reports: " + list.size());
			cur.close();
			closeDatabase();
			return list;
		}
	}
}
