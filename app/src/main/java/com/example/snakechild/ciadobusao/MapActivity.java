package com.example.snakechild.ciadobusao;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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


public class MapActivity extends Activity implements OnMapReadyCallback {

    private MapFragment mMapFragment;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private double latitude;
    private double longitude;
    public static Boolean novoEncontro = false;
    private LocationManager manager;
    private ImageButton confirmaLocalizacaoButton;
    private boolean zoomedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        customizeItems();
        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        zoomedIn = false;
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (novoEncontro) {
            confirmaLocalizacaoButton = (ImageButton) findViewById(R.id.idConfirmaLocalizacaoButton);
            confirmaLocalizacaoButton.setVisibility(View.VISIBLE);
            confirmaLocalizacaoButton.setClickable(true);
        }

    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //if gps is disabled
            displayPromptForEnablingGPS();
        }
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!zoomedIn) {
                    LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                    googleMap.animateCamera(yourLocation);
                    zoomedIn = true;
                }
            }
        });

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
                                Toast.makeText(getApplicationContext(), "Abrindo informações sobre o encontro...", Toast.LENGTH_SHORT).show();
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

    public void displayPromptForEnablingGPS() {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Ative o GPS para que possamos encontrar sua localização";

        builder.setMessage(message)
                .setPositiveButton(getString(R.string.bt_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int idButton) {
                                startActivity(new Intent(action));
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.bt_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int idButton) {
                                dialog.cancel();
                            }
                        });
        builder.create().show();
    }

    public void confirmaLocalizacao(View v) {
        NovoEncontroActivity.latitude = latitude;
        NovoEncontroActivity.longitude = longitude;
        novoEncontro = false;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
