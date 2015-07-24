package com.example.snakechild.ciadobusao;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.AdapterMeusEncontrosListView;
import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PerfilActivity extends BaseActivity {

    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static ProfilePictureView profilePictureView;
    private static TextView userNameView;
    protected static String idUsuario = "";
    private ListView mListView;
    private List<String> encontros = new ArrayList<String>();
    private List<String> idsEncontros = new ArrayList<String>();
    private AdapterMeusEncontrosListView adapter;
    private TextView meusEncontrosText;
    private List<ParseObject> encontrosParticipo = new ArrayList<ParseObject>();

    //Atributos para Menu Lateral (Obrigatorios)
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private List<String> userFriends = new ArrayList<String>();


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


        Parse.initialize(this, "r2Hs81lOwoi7YK9mby5m49409JuOx5EzpBULMNnP", "tdeLsjWBSqTd5KRCoULEScSbzKHpW80m2bpHQZzZ");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("Encontro", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });


        mListView = (ListView) findViewById(R.id.idMeusEncontroslistView);
        adapter = new AdapterMeusEncontrosListView(this, encontros);
        if (accessToken != null) {
            makeMeRequest(accessToken);
            mListView.setAdapter(adapter);
        }

        //Detalha um encontro
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String idEncontro = getIdEncontro(position);

                Intent i = new Intent();
                i.setClass(getApplicationContext(), DetalhesEncontroActivity.class);
                startActivity(i);
                DetalhesEncontroActivity.setEncontro(idEncontro);
            }
        });

        set();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMeusEncontros();
    }

    public String getIdEncontro(int position){
        return idsEncontros.get(position);
    }

    private void makeMeRequest(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject user, GraphResponse response) {
                if (user != null) {
                    profilePictureView.setProfileId(user.optString("id"));
                    userNameView.setText(user.optString("name"));
                    idUsuario = user.optString("id");
                    try {
                        JSONArray friends = (JSONArray) user.getJSONObject("friends").get("data");
                        for (int i = 0; i < friends.length(); i++) {
                            JSONObject aux = (JSONObject) friends.get(i);
                            userFriends.add(aux.get("id").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getMeusEncontros();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture,friends");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void getMeusEncontros() {
        getEncontrosParticipo();
        ParseQuery query = new ParseQuery("Encontro");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                encontros.clear();
                idsEncontros.clear();
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if (parseObjects.get(i).get("idDono").equals(idUsuario)){
                            String id = (String) parseObjects.get(i).getObjectId();
                            String encontro = (String) parseObjects.get(i).get("nome");
                            encontros.add(encontro);
                            idsEncontros.add(id);
                        } else {
                            for (int j = 0; j < encontrosParticipo.size(); j++) {
                                if (parseObjects.get(i).getObjectId().equals(encontrosParticipo.get(j).get("idEncontro"))) {
                                    String id = (String) parseObjects.get(i).getObjectId();
                                    String encontro = (String) parseObjects.get(i).get("nome");
                                    encontros.add(encontro);
                                    idsEncontros.add(id);
                                }
                            }
                        }
                    }
                }
                SystemClock.sleep(1000);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getEncontrosParticipo() {
        ParseQuery query = new ParseQuery("PerfisConfirmaram");
        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    encontrosParticipo.clear();
                    encontrosParticipo = parseObjects;
                }
            }
        });
    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", userNameView);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", meusEncontrosText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.backToPerfil.setVisibility(View.VISIBLE);
    }
}
