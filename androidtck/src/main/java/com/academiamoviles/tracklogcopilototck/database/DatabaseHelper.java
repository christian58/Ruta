package com.academiamoviles.tracklogcopilototck.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.academiamoviles.tracklogcopilototck.model.Incidences;
import com.academiamoviles.tracklogcopilototck.model.Paths;
import com.academiamoviles.tracklogcopilototck.model.Plates;
import com.academiamoviles.tracklogcopilototck.model.PlatesHasPaths;
import com.academiamoviles.tracklogcopilototck.model.Users;
import com.academiamoviles.tracklogcopilototck.ui.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by veraj on 2/08/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "database.db";
    public static  String DBLOCATION ="data/data/com.academiamoviles.tracklogcopilototck/databases/";
    private Context mContext;
    protected SQLiteDatabase mDatabase;



    public DatabaseHelper(Context context){
        super(context, DBNAME, null, 1);
        this.mContext = context;
        if(android.os.Build.VERSION.SDK_INT >= 17) {
            DBLOCATION = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DBLOCATION = "/data/data/" + context.getPackageName() + "/databases/";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void openDatabase(){
        String dbPath= mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()){
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath,null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if(mDatabase!=null){
            mDatabase.close();
        }
    }

    public int getLastIdPlatesHasPaths(int idPlates, int idPaths){
        int lastId;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("select * from plates_has_paths where idPlates ="+idPlates+" and idPaths ="+idPaths+" order by date desc ",null);
        cursor.moveToFirst();
        if(cursor.getCount()<=0 ){
            return 0;
        }
        lastId =cursor.getInt(0);
        closeDatabase();
        return lastId;

    }
    public int getLastIdPlatesHasPaths(int idPaths){
        int lastId;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("select * from plates_has_paths where idPaths ="+idPaths+" order by date desc ",null);
        cursor.moveToFirst();
        if(cursor.getCount()<=0 ){
            return 0;
        }
        lastId =cursor.getInt(0);
        closeDatabase();
        return lastId;
    }


    public int getGroups(int idPlates_has_Paths){
        int numGroup;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT DISTINCT idgroup FROM incidences WHERE idPlates_has_paths = "+idPlates_has_Paths+"  order by idgroup desc",null);
        cursor.moveToFirst();
        if(cursor.getCount()<=0 ){
            return 0;
        }
        numGroup =cursor.getInt(0);
        closeDatabase();
        return numGroup;
    }

    public List<Incidences> getIncidences(int idPlates_has_Paths) throws ParseException {
        Incidences incidences = null;
        List<Incidences> incidencesList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM incidences WHERE idPlates_has_paths = "+idPlates_has_Paths,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            //users = new Users(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(6));
            incidences = new Incidences(cursor.getInt(1),cursor.getInt(2), cursor.getInt(3),cursor.getDouble(4), cursor.getDouble(5), date, cursor.getInt(7));
            incidences.setIdIncidences(cursor.getInt(0));
            incidencesList.add(incidences);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return incidencesList;
    }


    public void createIncidence(Incidences incidences){
        openDatabase();
        mDatabase.execSQL("INSERT INTO incidences (idPlates_has_paths, speed, excess, latitude, longitude, register, idgroup) VALUES ('"+incidences.getIdPlates_has_paths()+"','"+incidences.getSpeed()+"','"+incidences.getExcess()+"','"+
                            incidences.getLatitude()+"','"+incidences.getLongitude()+"','"+Configuration.createFormat(incidences.getRegister())+"','"+ incidences.getIdgroup()+"')");

        closeDatabase();
    }

    public Paths getPathsByCodRuta(int codRuta){
        Paths paths = null;

        openDatabase();
        Cursor cursor =mDatabase.rawQuery("SELECT * FROM paths WHERE nCodRuta ="+codRuta, null);
        cursor.moveToFirst();
        paths = new Paths(cursor.getInt(1));
        paths.setIdPaths(cursor.getInt(0));
        cursor.close();
        closeDatabase();
        return paths;
    }

    public int createPlatePaths(PlatesHasPaths platesHasPaths){
        openDatabase();

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("idPlates", platesHasPaths.getIdPlates()+"");
        nuevoRegistro.put("idPaths",platesHasPaths.getIdPaths()+"");
        nuevoRegistro.put("date", Configuration.createFormat(platesHasPaths.getDate()));

        long idPltesHasPaths= mDatabase.insert("plates_has_paths", null, nuevoRegistro);

        //mDatabase.execSQL("INSERT INTO plates_has_paths (idPlates, idPaths, date) VALUES ('"+platesHasPaths.getIdPlates()+"','"+platesHasPaths.getIdPaths()+"','"+platesHasPaths.getDate().toString()+"')");

        //mDatabase

        closeDatabase();

        return (int) idPltesHasPaths;
    }


    public void createPaths(List<Paths> listPaths){
        openDatabase();
        for(int i=0; i<listPaths.size(); i++){
            if(!checkDataExist("paths","nCodRuta",listPaths.get(i).getnCodRuta())){
                mDatabase.execSQL("INSERT INTO paths (nCodRuta) VALUES ('"+listPaths.get(i).getnCodRuta()+"')");
                System.out.println("EXISTE Y REGISTRO NUEVA RUTA");
            }else{
                System.out.println("NO EXISTE Y ACTUALIZO LA RUTA");
            }

        }

        closeDatabase();
    }

    public List<Users> getListUsers(){
        Users users = null;
        List<Users> usersList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM users",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            //users = new Users(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

            users = new Users(cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4));
            users.setIdUsers(cursor.getInt(0));
            usersList.add(users);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return usersList;

    }


    public List<Plates> getListPlatesByUserID(Integer idUser){
        Plates plates = null;
        List<Plates> platesList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM plates WHERE idUsers = "+idUser,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            //users = new Users(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

            plates = new Plates(cursor.getString(1), cursor.getInt(2));

            plates.setIdPlates(cursor.getInt(0));
            platesList.add(plates);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return platesList;

    }

    public Users getUserByUsername(String username){
        Users users = null;

        openDatabase();
        Cursor cursor =mDatabase.rawQuery("SELECT * FROM users WHERE username ="+username, null);
        cursor.moveToFirst();
        users = new Users(cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4));
        users.setIdUsers(cursor.getInt(0));
        cursor.close();
        closeDatabase();
        return users;


    }

    public Plates getPlateBySerie(String serie){
        Plates plates = null;

        openDatabase();
        Cursor cursor =mDatabase.rawQuery("SELECT * FROM plates WHERE serie ='"+serie+"'", null);
        cursor.moveToFirst();
        plates = new Plates(cursor.getString(1),cursor.getInt(2));
        plates.setIdPlates(cursor.getInt(0));
        cursor.close();
        closeDatabase();
        return plates;

    }
    public Plates getPlateByIdPlatesHasPaths(int id){
        Plates plates = null;
        openDatabase();
        Cursor cursor =mDatabase.rawQuery("SELECT p.* FROM plates_has_paths pp JOIN plates p ON pp.idPlates=p.idPlates WHERE idPlates_has_paths=" + id, null);
        cursor.moveToFirst();
        plates = new Plates(cursor.getString(1),cursor.getInt(2));
        plates.setIdPlates(cursor.getInt(0));
        cursor.close();
        closeDatabase();
        return plates;
    }

    public void createUser(Users users){
        openDatabase();
        if(!checkDataExist("users","username",users.getUsername())){
            mDatabase.execSQL("INSERT INTO users (firstname,lastname,phone,username) VALUES ('"+users.getFirstname()+"','"+users.getLastname()+"','"+users.getPhone()+"','"+users.getUsername()+"')");

        }
        closeDatabase();
    }


    public void createPlate(Plates plates){
        openDatabase();
        if(!checkDataExistWithID("plates","serie", plates.getSerie(),"idUsers",plates.getIdUsers())){
            mDatabase.execSQL("INSERT INTO plates (serie, idUsers) VALUES ('"+plates.getSerie()+"','"+plates.getIdUsers()+"')");
        }
        closeDatabase();
    }


    public boolean checkDataExist(String tableName, String dbfield, String fieldValue) {
        String Query = "Select * from " + tableName + " where " + dbfield + " = '" + fieldValue+"'";
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkDataExist(String tableName, String dbfield, Integer fieldValueID) {
        String Query = "Select * from " + tableName + " where " + dbfield + " = " + fieldValueID;
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }




    public boolean checkDataExistWithID(String tableName, String dbfield, String fieldValue, String dbFieldID ,Integer fieldIDValue) {
        String Query = "Select * from " + tableName + " where " + dbfield + " = '" + fieldValue+"' and "+ dbFieldID+" = "+fieldIDValue;
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void createRegistersSintetic() throws ParseException {

        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:56:01");
        Incidences in1 = new Incidences(2,15,20,-16.407578, -71.524402,date1 ,1);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:56:11");
        Incidences in2 = new Incidences(2,15,22,-16.407578, -71.524402,date2 ,1);
        Date date3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:56:21");
        Incidences in3 = new Incidences(2,15,16,-16.407578, -71.524402,date3 ,1);

        Date date4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:56:31");
        Incidences in4 = new Incidences(2,15,30,-16.407578, -71.524402,date4 ,2);
        Date date5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:56:41");
        Incidences in5 = new Incidences(2,15,35,-16.407578, -71.524402,date5 ,2);
        Date date6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:56:51");
        Incidences in6 = new Incidences(2,15,33,-16.407578, -71.524402,date6 ,2);

        Date date7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:57:12");
        Incidences in7 = new Incidences(2,20,21,-16.407578, -71.524402,date7 ,3);
        Date date8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:57:22");
        Incidences in8 = new Incidences(2,20,22,-16.407578, -71.524402,date8 ,3);
        Date date9 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-24 12:57:32");
        Incidences in9 = new Incidences(2,20,23,-16.407578, -71.524402,date9 ,3);


        List<Incidences> listIn = new ArrayList<>();
        listIn.add(in1); listIn.add(in2); listIn.add(in3); listIn.add(in4); listIn.add(in5); listIn.add(in6); listIn.add(in7); listIn.add(in8); listIn.add(in9);

        openDatabase();

        for (Incidences incidences:listIn ) {
            mDatabase.execSQL("INSERT INTO incidences (idPlates_has_paths, speed, excess, latitude, longitude, register, idgroup) VALUES ('"+incidences.getIdPlates_has_paths()+"','"+incidences.getSpeed()+"','"+incidences.getExcess()+"','"+
                    incidences.getLatitude()+"','"+incidences.getLongitude()+"','"+Configuration.createFormat(incidences.getRegister())+"','"+ incidences.getIdgroup()+"')");

        }



        closeDatabase();
    }
}
