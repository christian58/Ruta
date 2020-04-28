package com.academiamoviles.tracklogcopilototck.ws.response;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.academiamoviles.tracklogcopilototck.util.InternalStorageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 07/02/2017.
 */

public class User implements Parcelable{

    @Expose
    @SerializedName("usuario_id")
    public String userId;

    @Expose
    @SerializedName("atributo")
    public int attribute;

    @Expose
    @SerializedName("usuarioWeb")
    public String userWeb;

    @Expose
    @SerializedName("idEmpresa")
    public String idEmpresa;

    @Expose
    @SerializedName("msg")
    public String msg;


    public User(){}

    protected User(Parcel in) {
        userId = in.readString();
        attribute = in.readInt();
        userWeb = in.readString();
        idEmpresa = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeInt(attribute);
        dest.writeString(userWeb);
        dest.writeString(idEmpresa);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



    public String toJson() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

    public static User fromJson(String json) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, User.class);
    }

    /**Almacenamiento de los datos del usuario en la memoria interna**/

    private static final String FILE_NAME = "User";

    public static void saveIS(Context context, String json) {
        InternalStorageUtils.save(context, FILE_NAME, json);
    }

    public static User readIS(Context context) {
        String json = InternalStorageUtils.read(context, FILE_NAME);

        if (json.equals(""))
            return null;

        return fromJson(json);
    }

    public static boolean deleteIS(Context context){
        return InternalStorageUtils.delete(context, FILE_NAME);
    }
}
