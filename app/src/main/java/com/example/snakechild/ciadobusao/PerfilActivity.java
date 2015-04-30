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
import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

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
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            makeMeRequest(accessToken);
        }

        //BD
        Parse.enableLocalDatastore(this);

        //Notificacao
        Parse.initialize(this, "r2Hs81lOwoi7YK9mby5m49409JuOx5EzpBULMNnP", "tdeLsjWBSqTd5KRCoULEScSbzKHpW80m2bpHQZzZ");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //EXAMPLE
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();
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
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
