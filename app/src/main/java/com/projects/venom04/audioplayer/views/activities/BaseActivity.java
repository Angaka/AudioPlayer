package com.projects.venom04.audioplayer.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.projects.venom04.audioplayer.models.services.MediaPlayerService;

/**
 * Created by Venom on 02/10/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent playerIntent = new Intent(BaseActivity.this, MediaPlayerService.class);
        startService(playerIntent);
    }
}
