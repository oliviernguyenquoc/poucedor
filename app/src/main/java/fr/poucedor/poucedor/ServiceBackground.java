package fr.poucedor.poucedor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

public class ServiceBackground extends Service {

    private LocationManager mLocationMgr;
    private LocationListener mOnLocationChange;
    private ResultReceiver mRec;
    private boolean mIsGpsEnabled;
    private boolean mIsNetworkEnabled;
    private static final long MIN_DISTANCE = 10; // 10 mètres avant de réactualiser
    private static final long MIN_TIME = 10000; // 10 secondes avant de rafraichir
    private Location mLocation;
    private boolean mIsStartedService;

    public static String RECEIVER_TAG = "receiverTag";


    public ServiceBackground() {
        mIsGpsEnabled = false;
        mIsNetworkEnabled = false;
        mIsStartedService = false;
        mLocation = null;

    }

    @Override
    public void onCreate() {
        initListener();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Récupération du receiver que l'activity a envoyé (permet de passé des bundles)
        mRec = intent.getParcelableExtra(ServiceBackground.RECEIVER_TAG);

        if (!mIsStartedService) {
            // Initialise et instancie le LocationManager (gestion des positions et providers)
            initLocationManager();

            mIsStartedService = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocationManager() {
        // Récupère une instance de LocationManager depuis le systeme
        mLocationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // Test des providers, dispo ou indispo ?
        mIsGpsEnabled = mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        mIsNetworkEnabled = mLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (mIsGpsEnabled) {
            mLocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mOnLocationChange);
            mLocation = mLocationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            if (mIsNetworkEnabled) {
                mLocationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mOnLocationChange);
                mLocation = mLocationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }


        if (mLocation != null) {
            post(0, mLocation.getLatitude(), mLocation.getLongitude());
        }

    }


    private void initListener() {
        mOnLocationChange = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                post(0, location.getLatitude(), location.getLongitude());
            }
        };

    }

    // Methode qui permet d'envoyer un bundle à l'activity
    private void post(int code, double latitude, double longitude) {
        if (!mRec.equals(null)) {
            Bundle b = new Bundle();
            b.putDouble("latitude", latitude);
            b.putDouble("longitude", longitude);
            mRec.send(code, b);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
