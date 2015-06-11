package com.example.snakechild.ciadobusao;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private MapFragment mMapFragment;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private double latitude;
    private double longitude;
    public static Boolean novoEncontro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        customizeItems();

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml

        set(navMenuTitles,navMenuIcons);

    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        if (novoEncontro) {
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMap.clear();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    googleMap.addMarker(new MarkerOptions()
                        .position(latLng).title(getIntent().getStringExtra("nomeEnc")));
                }
            });
        } else {
            getEncontros(googleMap);
        }

    }

    @Override
    public void onBackPressed() {
        NovoEncontroActivity.latitude = latitude;
        NovoEncontroActivity.longitude = longitude;
        novoEncontro = false;
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        novoEncontro = false;
        super.onDestroy();
    }

    private void getEncontros(final GoogleMap googleMap) {
        ParseQuery query = new ParseQuery("Encontro");
        /**
         * Falta inserir a seguranca aqui!
         */
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        double latitude = Double.parseDouble((String) parseObjects.get(i).get("latitude"));
                        double longitude = Double.parseDouble((String) parseObjects.get(i).get("longitude"));
                        LatLng latLng = new LatLng(latitude, longitude);
                        String nomeEncontro = (String) parseObjects.get(i).get("nome");
                        final String idEncontro = parseObjects.get(i).getObjectId();
                        final Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng).title(nomeEncontro).snippet("ID = " + idEncontro));


                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                marker.showInfoWindow();
                                Toast.makeText(getApplicationContext(), "Abrindo informa\u00e7\u00f5es sobre o encontro...", Toast.LENGTH_SHORT).show();
                                try {
                                    Thread.sleep(Toast.LENGTH_SHORT);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                Intent i = new Intent();
                                i.setClass(getApplicationContext(), DetalhesEncontroActivity.class);
                                startActivity(i);
                                DetalhesEncontroActivity.setEncontro(marker.getSnippet().replace("ID = ", ""));
                                return true;
                            }
                        });
                    }
                }
            }
        });
    }



}
