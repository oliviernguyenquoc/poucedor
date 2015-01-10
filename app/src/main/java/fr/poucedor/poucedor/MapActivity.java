/*
 * Adapted from OSMDroid Sample App - Apache License, Version 2.0
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;

import android.os.Bundle;
import android.graphics.drawable.Drawable;

import fr.poucedor.poucedor.UI.MyItemizedOverlay;

public class MapActivity extends BaseActivity {

    MyItemizedOverlay myItemizedOverlay = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        MapView mapView = (MapView) findViewById(R.id.mapview);

        MapController mapController = new MapController(mapView);

        mapController.setZoom(7);
        mapView.setMinZoomLevel(4);

        // Center over France
        double lat = 51 * 1000000;
        double lon = -5 * 1000000;
        GeoPoint p = new GeoPoint((int) lat, (int) lon);
        mapController.animateTo(p);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        Drawable marker = getResources().getDrawable(android.R.drawable.star_big_on);
        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();
        marker.setBounds(0, markerHeight, markerWidth, 0);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
        mapView.getOverlays().add(myItemizedOverlay);

        GeoPoint myPoint1 = new GeoPoint(0 * 1000000, 0 * 1000000);
        myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
        GeoPoint myPoint2 = new GeoPoint((int) 48.8534100 * 1000000, (int) 2.3488000 * 1000000);
        myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");

    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_MAP;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_map;
    }

}