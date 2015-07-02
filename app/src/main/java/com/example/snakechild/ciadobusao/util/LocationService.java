package com.example.snakechild.ciadobusao.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.snakechild.ciadobusao.PerfilActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by Pedro on 02/07/2015.
 */
public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TEN_SECONDS = 1000 * 10;
    public static final double RADIUS = 0.000200;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private String userId;
    private String encontroId;
    private String encontroLat;
    private String encontroLong;
    private double limitUp;
    private double limitBot;
    private double limitLeft;
    private double limitRight;

    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        userId = extras.getString("idUsuario");
        encontroId = extras.getString("idEncontro");
        encontroLat = extras.getString("encontroLat");
        encontroLong = extras.getString("encontroLong");
        criaQuadrado();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        return START_NOT_STICKY;
    }

    private void criaQuadrado() {
        double encLat = Float.valueOf(encontroLat);
        double encLg = Float.valueOf(encontroLong);
        limitBot = encLat - RADIUS;
        limitUp = encLat + RADIUS;
        limitLeft = encLg - RADIUS;
        limitRight = encLg + RADIUS;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TEN_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -TEN_SECONDS;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            if (isBetterLocation(loc, previousBestLocation)) {
                if (loc.getLatitude() > limitBot && loc.getLatitude() < limitUp) {
                    if (loc.getLongitude() > limitLeft && loc.getLongitude() < limitRight) {
                        Toast.makeText(getApplicationContext(), "CHEGUEI NO ENCONTRO", Toast.LENGTH_SHORT).show();
                        final ParseObject usuario = new ParseObject("PerfisChegaram");
                        usuario.put("idUsuario", userId);
                        usuario.put("idEncontro", encontroId);
                        usuario.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                onDestroy();
                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

    }
}
