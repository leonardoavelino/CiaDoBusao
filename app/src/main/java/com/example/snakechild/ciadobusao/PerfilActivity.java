package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class PerfilActivity extends Activity {

    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static ProfilePictureView profilePictureView;
    private static TextView userNameView;
    protected static String idUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        userNameView = (TextView) this.findViewById(R.id.selection_user_name);

        profilePictureView = (ProfilePictureView) this
                .findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        customizeItems();
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
            public void onCompleted(final JSONObject user, GraphResponse response) {
                if (user != null) {
                    profilePictureView.setProfileId(user.optString("id"));
                    userNameView.setText(user.optString("name"));
                    idUsuario = user.optString("id");
                    final ParseObject usuario = new ParseObject("Usuario");
                    ParseQuery query = new ParseQuery("Usuario");
                    query.whereEqualTo("id_gcm", user.optString("id"));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> profileList, ParseException e) {
                            if (e == null) {
                                if (profileList.size()==0){
                                    usuario.put("nome", user.optString("name"));
                                    usuario.put("id_gcm", user.optString("id"));
                                    usuario.put("url_foto", MainActivity.foto);
                                    usuario.saveInBackground();
                                    Log.d("usuario", "Usuario salvo");
                                }else{
                                    Log.d("usuario", "Usuario ja existe");
                                }

                            } else {
                                Log.d("usuario", "Error: " + e.getMessage());
                            }
                        }
                    });

                }
            }
        });
        request.executeAsync();

    }

    public void customizeItems(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", userNameView);
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
