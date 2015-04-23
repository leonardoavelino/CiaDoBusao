package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.widget.ProfilePictureView;


public class PerfilActivity extends Activity {

    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        profilePictureView = (ProfilePictureView) this
                .findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
    }

}
