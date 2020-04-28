package com.academiamoviles.tracklogcopilototck.ws.asynctask;

import android.util.Log;
import com.academiamoviles.tracklogcopilototck.util.Constants;
import com.academiamoviles.tracklogcopilototck.util.HttpPoster;

/**
 * Created by Android on 07/02/2017.
 */
public class HttpAsyncTask extends BaseAsyncTask<String, Void, String> {

    public static final String TAG =HttpAsyncTask.class.getCanonicalName();

    private String request ="";
    private String ws;

    /**
     *
     * @param ws Web service
     * @param request Request del web service
     */
    public HttpAsyncTask(String ws,String request){
        this.ws = ws;
        this.request = request;
    }

    public HttpAsyncTask(int requestCode){
        super(requestCode);
    }

    public void setWebService(String ws){
        this.ws = ws;
    }
    public void setRequest(String request){
        this.request = request;
    }

    protected String getRequest() {
        return request;
    }

    protected String getWs() {
        return ws;
    }

    @Override
    protected String doInBackground(String... params) {
        String resultado = null;

        HttpPoster httpPoster = new HttpPoster();
        httpPoster.setUrl(Constants.URL + getWs());
        httpPoster.setRequest(getRequest());

        try {
            httpPoster.invoke();
            resultado = httpPoster.getResponse();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Log.d(TAG, Constants.Mensaje.ERROR_SC);
        }
        super.onPostExecute(result);
    }
}
