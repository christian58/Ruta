package com.academiamoviles.tracklogcopilotommg.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.academiamoviles.tracklogcopilotommg.R;

/**
 * Created by Fernando-PC on 21/02/2017.
 */

public class UserUtil {

    public static void saveCredentials(Activity context, String user, String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.user), user);
        editor.putString(context.getString(R.string.pass), password);
        editor.apply();
    }

    public static String getUser(Activity context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.user), "");
    }
    public static String getPassword(Activity context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pass), "");
    }

    public static String getUserWeb(Activity context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.user_web), "");
    }
}
