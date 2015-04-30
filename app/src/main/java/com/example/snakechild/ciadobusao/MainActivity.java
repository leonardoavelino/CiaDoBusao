package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        final LoginButton authButton = (LoginButton) findViewById(R.id.auth_button);
        authButton.setReadPermissions("public_profile");
        authButton.setReadPermissions("user_friends");
        authButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Log.i("name", object.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture");
                request.setParameters(parameters);
                request.executeAsync();
                Intent i = new Intent();
                i.setClass(getApplicationContext(), PerfilActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                Log.i("map", "cancel");

            }

            @Override
            public void onError(FacebookException e) {
                Log.i("map", e.toString());

            }
        });
        // Hide the Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Customize font
        TextView txt = (TextView) findViewById(R.id.idAppName);
        Typeface font = Typeface.createFromAsset(getAssets(), "Amaranth-Bold.otf");
        txt.setTypeface(font);
        if (authButton.getText().toString().equals("Log out")) {
            Intent i = new Intent();
            i.setClass(getApplicationContext(), PerfilActivity.class);
            startActivity(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
