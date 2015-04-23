package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class PerfilActivity extends Activity {

    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static ProfilePictureView profilePictureView;
    private static TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        userNameView = (TextView) this.findViewById(R.id.selection_user_name);
        //userNameView.setText(user.getName());
        profilePictureView = (ProfilePictureView) this
                .findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        //profilePictureView.setProfileId(user.getId());
        //profilePictureView.setDrawingCacheEnabled(true);
        //Bitmap foto = profilePictureView.getDrawingCache();
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //foto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //byte[] b = baos.toByteArray();

    }

}
