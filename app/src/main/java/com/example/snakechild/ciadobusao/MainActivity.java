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

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends Activity {

    CallbackManager mCallbackManager;
    private static String foto;
    private static TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "r2Hs81lOwoi7YK9mby5m49409JuOx5EzpBULMNnP", "tdeLsjWBSqTd5KRCoULEScSbzKHpW80m2bpHQZzZ");

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
                                    final JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Log.i("name", object.getString("name"));
                                    foto = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    final ParseObject usuario = new ParseObject("Usuario");
                                    ParseQuery query = new ParseQuery("Usuario");
                                    query.whereEqualTo("id_user", object.optString("id"));
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> profileList, ParseException e) {
                                            if (e == null) {
                                                if (profileList.size()==0){
                                                    usuario.put("nome", object.optString("name"));
                                                    usuario.put("id_user", object.optString("id"));
                                                    usuario.put("url_foto", foto);
                                                    usuario.saveInBackground();
                                                    Log.d("usuario", "Usuario salvo");
                                                }else{
                                                    Log.d("usuario", "Usuario ja existe");
                                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuario");
                                                    query.getInBackground(profileList.get(0).getObjectId(), new GetCallback<ParseObject>() {
                                                        public void done(ParseObject user, ParseException e) {
                                                            if (e == null) {
                                                                user.put("nome", object.optString("name"));
                                                                user.put("url_foto", foto);
                                                                user.saveInBackground();
                                                                Log.d("usuario", "Usuario atualizado");

                                                            }
                                                        }
                                                    });
                                                }

                                            } else {
                                                Log.d("usuario", "Error: " + e.getMessage());
                                            }
                                        }
                                    });

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

        customizeItems();

        if (authButton.getText().toString().equals("Log out")) {
            Intent i = new Intent();
            i.setClass(getApplicationContext(), PerfilActivity.class);
            startActivity(i);
        }
    }

    public void customizeItems(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        appName = (TextView) findViewById(R.id.idAppName);
        CustomizeFont.customizeFont(this, "Amaranth-Bold.otf", appName);
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
