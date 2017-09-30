package com.projects.venom04.audioplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projects.venom04.audioplayer.models.pojo.Audio;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Venom on 27/09/2017.
 */

public class StorageUtil {

    public static final String STORAGE = "com.projects.venom04.audioplayer.STORAGE";
    private SharedPreferences mPreferences;
    private Context mContext;

    public StorageUtil(Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }

    public void storeAudios(ArrayList<Audio> audios) {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(audios);
        editor.putString("audios", json);
        editor.apply();
    }

    public ArrayList<Audio> loadAudios() {
        Gson gson = new Gson();
        String json = mPreferences.getString("audios", null);
        Type type = new TypeToken<ArrayList<Audio>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void storeAudioIndex(int index) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
    }

    public int loadAudioIndex() {
        return mPreferences.getInt("audioIndex", -1);
    }

    public void clearCachedAudioPlaylist() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
