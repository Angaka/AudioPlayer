package com.projects.venom04.audioplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venom on 08/09/2017.
 */

public class PermissionsService {

    private static final String TAG = "PermissionsService";

    private Context mContext;
    private static SharedPreferences mPreferences;

    public PermissionsService(Context context, SharedPreferences preferences) {
        mContext = context;
        mPreferences = preferences;
    }

    private boolean hasPermission(String permission) {
        return (mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean shouldWeAsk(String permission) {
        return (mPreferences.getBoolean(permission, true));
    }

    public void markAsAsked(String permission) {
        mPreferences.edit().putBoolean(permission, false).apply();
    }

    public ArrayList<String> findUnAskedPermissions(List<String> wanted) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < wanted.size(); i++) {
            String permission = wanted.get(i);
            if (!hasPermission(permission) && shouldWeAsk(permission)) {
                results.add(permission);
            }
        }
        return results;
    }

    public ArrayList<String> findUnacceptedPermissions(List<String> wanted) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < wanted.size(); i++) {
            String permission = wanted.get(i);
            if (!hasPermission(permission)) {
                results.add(permission);
            }
        }
        return results;
    }
}
