package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;


public class PerfilActivity extends Activity {

    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static ProfilePictureView profilePictureView;
    private static TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Hide the Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userNameView = (TextView) this.findViewById(R.id.selection_user_name);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", userNameView);
        profilePictureView = (ProfilePictureView) this
                .findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            makeMeRequest(accessToken);
        }
    }

    public void criarNovoEncontro(View v){
        Intent i = new Intent();
        i.setClass(getApplicationContext(), NovoEncontroActivity.class);
        startActivity(i);

    }

    private void makeMeRequest(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse response) {
                if (user != null) {
                    profilePictureView.setProfileId(user.optString("id"));
                    userNameView.setText(user.optString("name"));
                }
            }
        });
        request.executeAsync();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
