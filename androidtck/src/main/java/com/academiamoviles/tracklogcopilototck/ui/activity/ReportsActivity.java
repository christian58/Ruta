package com.academiamoviles.tracklogcopilototck.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;
import com.academiamoviles.tracklogcopilototck.model.Coordinates;
import com.academiamoviles.tracklogcopilototck.model.Duration;
import com.academiamoviles.tracklogcopilototck.model.Excess;
import com.academiamoviles.tracklogcopilototck.model.Incidences;
import com.academiamoviles.tracklogcopilototck.model.Plates;
import com.academiamoviles.tracklogcopilototck.model.Ubication;
import com.academiamoviles.tracklogcopilototck.model.Users;
import com.academiamoviles.tracklogcopilototck.ui.Configuration;
import com.academiamoviles.tracklogcopilototck.util.Constants;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta_Response;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.view.Gravity.CENTER;

public class ReportsActivity extends BaseActivity {


    RelativeLayout mainLayout;

    TextView txtDriver;
    TextView txtNameDriver;
    TextView txtDriverNameLabel;
    TextView txtDriverName;
    TextView txtPath;
    TextView txtPathName;
    TextView txtExcess;
    TextView txtExcessTotal;
    TextView txtTime;
    TextView txtTimeTotal;
    TextView txtPlate;
    TextView txtPlateName;

    LinearLayout linearDriver;
    LinearLayout linearDriverName;
    LinearLayout linearPath;
    LinearLayout linearExcess;
    LinearLayout linearTime;
    LinearLayout linearPlate;

    TableLayout tableReports;
    LinearLayout linearTable;
    ScrollView scrollView;
    DatabaseHelper mDBHelper;
    long totalSeconds =0;
    long totalPromedio =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initSizeWindow();
        mainLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        mDBHelper = new DatabaseHelper(this);
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //setContentView(mainLayout);
    }
    public void initSizeWindow(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthInDp = size.x;
        int heightInDp = size.y;
        Configuration.setWidthPixel (widthInDp);
        Configuration.setHeigthPixel (heightInDp);
    }



    public void initView() throws ParseException {


      //  mainLayout = new RelativeLayout(this);

        txtDriver= new TextView(this);
        txtNameDriver= new TextView(this);
        txtDriverNameLabel= new TextView(this);
        txtDriverName= new TextView(this);
        txtPath= new TextView(this);
        txtPathName= new TextView(this);
        txtExcess= new TextView(this);
        txtExcessTotal= new TextView(this);
        txtTime= new TextView(this);
        txtTimeTotal= new TextView(this);
        txtPlate = new TextView(this);
        txtPlateName = new TextView(this);

        linearDriver = new LinearLayout(this);
        linearDriverName = new LinearLayout(this);
        linearPath = new LinearLayout(this);
        linearExcess = new LinearLayout(this);
        linearTime = new LinearLayout(this);
        linearPlate = new LinearLayout(this);

        tableReports = new TableLayout(this);
        linearTable = new LinearLayout(this);
        scrollView = new ScrollView(this);


      //  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    //    mainLayout.setLayoutParams(params);

        LinearLayout.LayoutParams tableParams= new LinearLayout.LayoutParams(Configuration.getWidth(580), ViewGroup.LayoutParams.WRAP_CONTENT);//(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tableReports.setLayoutParams(tableParams);

        ScrollView.LayoutParams paramScroll = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Configuration.getHeight(500));
        scrollView.setLayoutParams(paramScroll);

        LinearLayout.LayoutParams paramLinear = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearDriver.setLayoutParams(paramLinear);
        linearDriverName.setLayoutParams(paramLinear);
        linearPath.setLayoutParams(paramLinear);
        linearExcess.setLayoutParams(paramLinear);
        linearTime.setLayoutParams(paramLinear);
        linearTable.setLayoutParams(paramLinear);

        linearDriver.setOrientation(LinearLayout.HORIZONTAL);
        linearDriverName.setOrientation(LinearLayout.HORIZONTAL);
        linearPath.setOrientation(LinearLayout.HORIZONTAL);
        linearExcess.setOrientation(LinearLayout.HORIZONTAL);
        linearTime.setOrientation(LinearLayout.HORIZONTAL);

        linearTable.setOrientation(LinearLayout.HORIZONTAL);
        linearTable.setGravity(CENTER);



        int sizeTxtDefault= 27;
        int sizeTxtPadding= 35;
        txtDriver.setText(getSpanString("Usuario: "));
        txtDriver.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtDriverNameLabel.setText(getSpanString("Conductor: "));
        txtDriverNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtPath.setText(getSpanString("Ruta: "));
        txtPath.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtExcess.setText(getSpanString("Exceso de Veloc Total: "));
        txtExcess.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtTime.setText(getSpanString("Tiempo total Exceso: "));
        txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtPlate.setText(getSpanString("Placa: "));
        txtPlate.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));

        txtDriver.setPadding(Configuration.getWidth(sizeTxtPadding),0,0,0);
        txtDriverNameLabel.setPadding(Configuration.getWidth(sizeTxtPadding),0,0,0);
        txtPath.setPadding(Configuration.getWidth(sizeTxtPadding),0,0,0);
        txtExcess.setPadding(Configuration.getWidth(sizeTxtPadding),0,0,0);
        txtTime.setPadding(Configuration.getWidth(sizeTxtPadding),0,0,0);
        txtPlate.setPadding(Configuration.getWidth(sizeTxtPadding),0,0,0);


        txtNameDriver.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtDriverName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtPathName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtExcessTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtTimeTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));
        txtPlateName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Configuration.getHeight(sizeTxtDefault));

        Users user = mDBHelper.getUserByUsername(myApp.objUser.userWeb);
        txtNameDriver.setText(getSpanString(user.getFirstname()));
        SharedPreferences prefs = getSharedPreferences(Constants.USER_PREFS_NAME, 0);
        txtDriverName.setText(getSpanString("" + prefs.getString(Constants.DRIVER_NAME_KEY, "")));
        Ruta_Response ruta_response = Ruta_Response.readIS(this);
        String rutaName = "";
		if (ruta_response != null && !ruta_response.listaRutas.isEmpty()) {
			for (Ruta r : ruta_response.listaRutas) {
				if (r.nCodRuta == Configuration.COD_RUTA) {
					rutaName = (r.cDescripRuta != null) ? r.cDescripRuta : "";
					break;
				}
			}
		}
        txtPathName.setText(getSpanString(rutaName + " (Ruta " + Configuration.COD_RUTA + ")"));
        txtExcessTotal.setText(getSpanString("0km/h"));
        txtTimeTotal.setText(getSpanString("0m"));
        String serie = "";
        if (Configuration.IDPLATES_HAS_PATHS != 0) {
        	Plates plate = mDBHelper.getPlateByIdPlatesHasPaths(Configuration.IDPLATES_HAS_PATHS);
        	if (plate != null)
        		serie = plate.getSerie();
        }
        txtPlateName.setText(getSpanString(serie));


        linearDriver.addView(txtDriver);
        linearDriver.addView(txtNameDriver);
        linearDriverName.addView(txtDriverNameLabel);
        linearDriverName.addView(txtDriverName);
        linearPath.addView(txtPath);
        linearPath.addView(txtPathName);
        linearExcess.addView(txtExcess);
        linearExcess.addView(txtExcessTotal);
        linearTime.addView(txtTime);
        linearTime.addView(txtTimeTotal);
        linearPlate.addView(txtPlate);
        linearPlate.addView(txtPlateName);


        /**
         * CREATION OF TABLE REPORTS
         * **/

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Configuration.getHeight(50));
        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(Configuration.getWidth(145), ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lineParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(this);
        row.setLayoutParams(rowParams);

        TextView txtColSpeed = new TextView(this);
        txtColSpeed.setText("Velocidad");
        txtColSpeed.setLayoutParams(txtParams);
        txtColSpeed.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);


        TextView txtColExcess = new TextView(this);
        txtColExcess.setText("Exceso");
        txtColExcess.setLayoutParams(txtParams);
        txtColExcess.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        TextView txtColDuration = new TextView(this);
        txtColDuration.setText("Duración");
        txtColDuration.setLayoutParams(txtParams);
        txtColDuration.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        TextView txtColUbication = new TextView(this);
        txtColUbication.setText("Ubicación");
        txtColUbication.setLayoutParams(txtParams);
        txtColUbication.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        row.addView(txtColSpeed);
        row.addView(txtColExcess);
        row.addView(txtColDuration);
        row.addView(txtColUbication);

        tableReports.addView(row);

        TableRow rowLine = new TableRow(this);
        rowLine.setLayoutParams(lineParams);

        LinearLayout line =new LinearLayout(this);
        LinearLayout.LayoutParams lineLinearParams = new LinearLayout.LayoutParams(Configuration.getWidth(580), 2);
        line.setLayoutParams(lineLinearParams);
        line.setBackgroundColor(Color.BLACK);

        tableReports.addView(line);



        createTableReports();



        linearTable.addView(tableReports);
        scrollView.addView(linearTable);


        linearDriver.setY(120);
        linearDriverName.setY(200);
        linearPlate.setY(280);
        linearPath.setY(360);
        linearExcess.setY(440);
        linearTime.setY(520);
        scrollView.setY(600);

        mainLayout.addView(linearDriver);
        mainLayout.addView(linearDriverName);
        mainLayout.addView(linearPlate);
        mainLayout.addView(linearPath);
        mainLayout.addView(linearExcess);
        mainLayout.addView(linearTime);
        mainLayout.addView(scrollView);



    }

    public void createTableReports() throws ParseException {

       // System.out.println("IDPLATES_HAS_PATHS = " +Configuration.IDPLATES_HAS_PATHS);
        List<Incidences> incidencesList= mDBHelper.getIncidences(Configuration.IDPLATES_HAS_PATHS);

        //System.out.println(Configuration.IDPLATES_HAS_PATHS);
        int numGroups= mDBHelper.getGroups(Configuration.IDPLATES_HAS_PATHS);

        Excess promExcess[] = new Excess[numGroups];
        Integer speed [] = new Integer[numGroups];
        Duration duration[] = new Duration[numGroups];
        final Ubication ubication[] = new Ubication[numGroups];


        for(int i=0; i<promExcess.length; i++){
            promExcess[i] = new Excess();
            speed[i] = new Integer(0);
            duration[i] = new Duration();
            ubication[i] = new Ubication();

        }


        for(int i=0; i<incidencesList.size(); i++){

            promExcess[incidencesList.get(i).getIdgroup()-1].getListExcess().add( incidencesList.get(i).getExcess());
            speed[incidencesList.get(i).getIdgroup()-1] = incidencesList.get(i).getSpeed();
            duration[incidencesList.get(i).getIdgroup()-1].getListDate().add(incidencesList.get(i).getRegister());
            ubication[incidencesList.get(i).getIdgroup()-1].getListCoordinates().add(new Coordinates(incidencesList.get(i).getLatitude(), incidencesList.get(i).getLongitude()));
           // System.out.println("Incidences> speed: " +incidencesList.get(i).getSpeed()+" excess: "+incidencesList.get(i).getExcess()+" date: "+incidencesList.get(i).getRegister()+" ubication: "+incidencesList.get(i).getLatitude()+", "+incidencesList.get(i).getLongitude()+ " group: "+incidencesList.get(i).getIdgroup());
        }




        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Configuration.getHeight(70));
        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(Configuration.getWidth(145), Configuration.getHeight(70));


        for(int i=0; i<numGroups; i++){
            TableRow row = new TableRow(this);
            row.setLayoutParams(rowParams);

            TextView txtColSpeed = new TextView(this);
            txtColSpeed.setText(speed[i]+"km/h");
            txtColSpeed.setLayoutParams(txtParams);
            txtColSpeed.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            TextView txtColExcess = new TextView(this);
            txtColExcess.setText(calcularPromedio(promExcess[i].getListExcess())+"km/h");
            txtColExcess.setLayoutParams(txtParams);
            txtColExcess.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);


            TextView txtColDuration = new TextView(this);
            txtColDuration.setText(calcularDuration(duration[i].getListDate()));
            txtColDuration.setLayoutParams(txtParams);
            txtColDuration.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            TextView txtColUbication = new TextView(this);

            SpannableString content = new SpannableString("Ver");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            txtColUbication.setText(content);
            txtColUbication.setLayoutParams(txtParams);
            txtColUbication.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            final int finalI = i;
            txtColUbication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intentMap = new Intent(getApplicationContext(), MapsActivity.class);
                    intentMap.putExtra("listCoordinates",(Serializable) ubication[finalI].getListCoordinates());
                    getApplicationContext().startActivity(intentMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            row.addView(txtColSpeed);
            row.addView(txtColExcess);
            row.addView(txtColDuration);
            row.addView(txtColUbication);
            tableReports.addView(row);
            LinearLayout line =new LinearLayout(this);
            LinearLayout.LayoutParams lineLinearParams = new LinearLayout.LayoutParams(Configuration.getWidth(580), 1);
            line.setLayoutParams(lineLinearParams);
            line.setBackgroundColor(Color.BLACK);
            tableReports.addView(line);
        }

        txtTimeTotal.setText(getFormatTotalDuration());
        if(totalPromedio ==0 || promExcess.length ==0){
            txtExcessTotal.setText("0 km/h");

        }else{
            txtExcessTotal.setText((totalPromedio/promExcess.length)+" km/h");

        }


    }

    public String calcularDuration(List<Date> list){
        String format="";
        if(list.size()==1){
            return "10 seg";
        }
        Date dateIni = list.get(0);
        Date dateFin = list.get(list.size()-1);
        long seconds = getDateDiff(dateIni, dateFin, TimeUnit.SECONDS);
        totalSeconds+=seconds;
        int formatTime []= splitToComponentTimes(seconds);
     //   System.out.println("H: "+formatTime[0] + "M: "+formatTime[1] +"S: "+formatTime[2]);

        if(formatTime[0]!=0){
            format+=formatTime[0]+"h ";
        }
        if(formatTime[1]!=0){
            format+=formatTime[1]+"m ";
        }
        if(formatTime[2]!=0){
            format+=formatTime[2]+"s";
        }

      //  System.out.println("FORMAT: "+format);

        return  format;


    }

    public String getFormatTotalDuration(){
        String format="";
     //   System.out.println("TOTAL FINAL "+totalSeconds);
        int formatTime []= splitToComponentTimes(totalSeconds);
     //   System.out.println("H: "+formatTime[0] + "M: "+formatTime[1] +"S: "+formatTime[2]);


        if(formatTime[0]!=0){
            format+=formatTime[0]+"h ";
        }
        if(formatTime[1]!=0){
            format+=formatTime[1]+"m ";
        }
        if(formatTime[2]!=0){
            format+=formatTime[2]+"s";
        }
        if(format.equals("")){
            format+="0s";
        }

        return  format;

    }

    public int calcularPromedio(List<Integer> list){
        int sum=0;
        for(int i=0; i<list.size(); i++){
            sum+= list.get(i);
        }
        int promedio =sum/list.size();
        totalPromedio+= promedio;
        return promedio;

    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(diffInMillies);
    }

    public static int[] splitToComponentTimes(long biggy)
    {
        long longVal = biggy;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }

    public SpannableString getSpanString(String s){
        SpannableString spanString = new SpannableString(s);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        return spanString;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
