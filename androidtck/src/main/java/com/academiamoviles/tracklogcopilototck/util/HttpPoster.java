package com.academiamoviles.tracklogcopilototck.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPoster {

    public static int TIMEOUT = 0;

    private static String TAG = HttpPoster.class.getCanonicalName();
    private int intentos = 3;
    private String url;
    private String request;
    private String response;
    private String contentType = "application/json";
    //private String contentType = "application/x-www-form-urlencoded";

    private String accept = "application/json";
    private int timeOut = 1000 * 30;
    private int status;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public int getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getResponse() {
        return response;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    private String executeHttpPostWithRetry() throws Exception {

        this.status = -1;
        Log.d(TAG, String.format("STATUS:%s", this.status));

        URL url = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if (TIMEOUT == 0) {
            conn.setReadTimeout(this.timeOut);
            conn.setConnectTimeout(this.timeOut);
        } else {
            conn.setReadTimeout(TIMEOUT);
            conn.setConnectTimeout(TIMEOUT);
        }

        Log.d(TAG, String.format("TIMEOUT:%s", this.timeOut));

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", this.contentType);
        //conn.setRequestProperty("Pragma", "no-cache");
        //conn.setRequestProperty("Cache-Control", "no-cache");
        //conn.setRequestProperty("Expires", "-1");
        conn.setRequestProperty("Accept", this.accept);
        //conn.setRequestProperty("Content-Length", "length");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        BufferedReader in = null;

        try {

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(this.request);
            writer.flush();
            writer.close();
            os.close();

            //Log.d(TAG, String.format("RQ:%s", this.request));
            //Log.d(TAG, String.format("URL:%s", this.url));

            if (conn.getOutputStream() != null) {
                Log.d(TAG, String.format("UPLOADBYTES:%s byte", this.request.getBytes().length));
            } else {
                Log.d(TAG, String.format("UPLOADBYTES:%s byte", 0));
            }

            this.status = conn.getResponseCode();
            Log.d(TAG, String.format("STATUS:%s", this.status));

            InputStream is = conn.getInputStream();

            if (HttpURLConnection.HTTP_OK == this.status) {

                char[] buffer = new char[8 * 1024];
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                Writer writer1 = new StringWriter();

                int n;
                while ((n = in.read(buffer)) != -1) {
                    writer1.write(buffer, 0, n);
                }

                in.close();

                StringBuffer sf = new StringBuffer(writer1.toString());
                //Log.d(TAG, String.format("RESPONSE:%s", sf.toString()));
                Log.d(TAG, String.format("DONWLOADBYTES:%s byte", sf.toString().getBytes().length));
                return sf.toString();

            } else {
                String msg = String.format("Error invocando Web Service - %s",
                        this.status);
                Log.d(TAG, msg);
                throw new Exception(msg);
            }
        } catch (Exception ex) {
            this.status = -1;
            throw ex;
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, String.valueOf(this.status));
                }
            }

            if (conn != null) {
                Log.d(TAG, "close http connection");
                conn.disconnect();

            }
        }
    }

    public void invoke() throws Exception {

        int intento = 0;
        while (intento < intentos) {
            intento += 1;

            Log.d("HttpPoster:", String.format("Intento %s", intento));

            try {
                this.response = executeHttpPostWithRetry();
                //Log.d(TAG, response);
                break;
            } catch (Exception e) {

                Log.e("invoke", String.format("%s", intento), e);
                this.response = null;
                if (intento < intentos) {

                } else {
                    throw e;
                }
            }

        }
    }
}