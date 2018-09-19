package aprivate.study.testnavermaps;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;

public class MainActivity extends NMapActivity {

    private NMapView mMapView;
    private NMapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mMapView = (NMapView)findViewById(R.id.mapView);

        // set a registered Client Id for Open MapViewer Library
        mMapView.setClientId(getString(R.string.naver_maps_client_id));

        // initialize map view
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        // register listener for map state changes
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        // use built in zoom controls
        mMapView.setBuiltInZoomControls(true, null);
        mMapView.displayZoomControls(true);

        super.setMapDataProviderListener(onDataProviderListener);

    }

    private static final String LOG_TAG = "NMapViewer";
    private static final boolean DEBUG = false;

    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
//                mMapController.setMapViewTrafficMode(false);
//                mMapController.setMapViewBicycleMode(false);
                mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 13);

            } else { // fail
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

                Toast.makeText(MainActivity.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }
    };

    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {
        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            NGeoPoint mapCenter = mapView.getMapController().getMapCenter();

            if(mapCenter != null) {
//                Log.i(LOG_TAG, "onTouchUp: center=" + mapCenter.longitude + "/" + mapCenter.latitude);

                setTextView(R.id.lng_data_tv, Double.toString(mapCenter.longitude));
                setTextView(R.id.lat_data_tv, Double.toString(mapCenter.latitude));
                findPlacemarkAtLocation(mapCenter.longitude, mapCenter.latitude);
            } else {
                setTextView(R.id.lng_data_tv, null);
                setTextView(R.id.lat_data_tv, null);

            }
        }

    };

    /* NMapDataProvider Listener */
    private final OnDataProviderListener onDataProviderListener = new OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (errInfo != null) {
                Log.i(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(MainActivity.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                setTextView(R.id.addr_data_tv, "주소변환 에러");
            } else {
                setTextView(R.id.addr_data_tv, placeMark.toString());
            }
        }

    };

    private void setTextView(int resourceId, @Nullable String text) {
        TextView textView = findViewById(resourceId);
        textView.setText(text == null ? "" : text);
    }

}
