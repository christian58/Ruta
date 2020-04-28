package com.academiamoviles.tracklogcopilotommg.ws.asynctask;

import android.os.AsyncTask;

/**
 * Created by Android on 07/02/2017.
 */
abstract public class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    /** Standard HttpAsynctask result: operation canceled. */
    public static final int RESULT_CANCELED    = 0;
    /** Standard HttpAsynctask result: operation succeeded. */
    public static final int RESULT_OK           = 1;
    /** Start HttpAsynctask result: operation error. */
    public static final int RESULT_ERROR   = -1;

    public static final int ERROR_DATOS   = -2;

    protected Callback callback;
    protected int requestCode = 0;

    public BaseAsyncTask(){
    }

    public BaseAsyncTask(int requestCode){
        this.requestCode = requestCode;
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    @Override
    protected Result doInBackground(Params... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        if(callback!=null)
            callback.onPostExecuteTask(requestCode, result==null ? RESULT_ERROR : RESULT_OK,result);
    }

    public interface Callback {
        void onPostExecuteTask(int requestCode, int resultCode, Object result);
    }
}
