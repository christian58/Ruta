package com.academiamoviles.tracklogcopilotommg;

import android.app.Application;

import com.academiamoviles.tracklogcopilotommg.ui.UsoApp;
import com.academiamoviles.tracklogcopilotommg.util.Util;
import com.academiamoviles.tracklogcopilotommg.ws.response.Cerco_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.Poi_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.Ruta_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.User;

/**
 * Created by Android on 07/02/2017.
 */

public class MyApp extends Application {

    public User objUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Util.setUrl(this);
        objUser = new User();
    }

    public void clearApp() {
        clearData();
        objUser = new User();
        User.deleteIS(this);
    }

    public void clearData() {
        Ruta_Response.deleteIS(this);
        Cerco_Response.deleteIS(this);
        Poi_Response.deleteIS(this);
        UsoApp.deleteIS(this);
    }
}
