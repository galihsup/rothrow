package rot.user.tekno.com.rothrow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import rot.user.tekno.com.rothrow.model.ListPengambil;

public class PengambilActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    float zoomLevel = 16.0f;
    RelativeLayout tlTampil;

    List<ListPengambil> lstPengambil = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengambil);
        tlTampil = (RelativeLayout) findViewById(R.id.layoutPopup);
        //lstPengambil.add(new ListPengambil(-6.252774, 106.8469302, "satu"));
        //lstPengambil.add(new ListPengambil(-6.252274, 106.8469102, "dua"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPengambil);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Jakarta and move the camera
        LatLng jakarta = new LatLng(-6.252884, 106.8469404);
        //mMap.addMarker(new MarkerOptions().position(jakarta).title("Marker in Jakarta"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, zoomLevel));
        addMarker();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                tlTampil.setVisibility(View.VISIBLE);
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                tlTampil.setVisibility(View.GONE);
            }
        });
    }

    public void addMarker(){
        mMap.clear();
        for (ListPengambil dataPengambil: lstPengambil) {

            LatLng marker = new LatLng(dataPengambil.getLat(), dataPengambil.getLang());
            //mMap.addMarker(new MarkerOptions().position(marker).title(dataPengambil.getJudul()));
        }
    }
}
