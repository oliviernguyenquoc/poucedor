package fr.poucedor.poucedor;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class MapActivity extends Activity {

    MyItemizedOverlay myItemizedOverlay = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();
        marker.setBounds(0, markerHeight, markerWidth, 0);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
        mapView.getOverlays().add(myItemizedOverlay);

        GeoPoint myPoint1 = new GeoPoint(0*1000000, 0*1000000);
        myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
        GeoPoint myPoint2 = new GeoPoint(50*1000000, 50*1000000);
        myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");

    }

}