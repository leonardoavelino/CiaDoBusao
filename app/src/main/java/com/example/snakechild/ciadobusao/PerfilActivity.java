package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PerfilActivity extends Activity {

    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static ProfilePictureView profilePictureView;
    private static TextView userNameView;
    protected static String idUsuario = "";
    private ListView mListView;
    private List<String> encontros = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private TextView meusEncontrosText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        userNameView = (TextView) this.findViewById(R.id.selection_user_name);

        profilePictureView = (ProfilePictureView) this
                .findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);

        meusEncontrosText = (TextView) this.findViewById(R.id.idMeusEncontrosTextView);
        customizeItems();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        mListView = (ListView) findViewById(R.id.idMeusEncontroslistView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, encontros);
        if (accessToken != null) {
            makeMeRequest(accessToken);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMeusEncontros();
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
                    getMeusEncontros();


                }
            }
        });
        request.executeAsync();

    }

    private void getMeusEncontros() {
        ParseQuery query = new ParseQuery("Encontro");
        query.whereEqualTo("idDono", idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                encontros.clear();
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        String encontro = (String) parseObjects.get(i).get("nome");
                        encontros.add(i, encontro);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void customizeItems(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", userNameView);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", meusEncontrosText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
