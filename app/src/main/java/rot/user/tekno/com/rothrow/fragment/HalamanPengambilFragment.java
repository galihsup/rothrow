package rot.user.tekno.com.rothrow.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rot.user.tekno.com.rothrow.AppsController;
import rot.user.tekno.com.rothrow.R;
import rot.user.tekno.com.rothrow.model.ListPengambil;
import rot.user.tekno.com.rothrow.util.Constant;
import rot.user.tekno.com.rothrow.util.MLocation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HalamanPengambilFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    float zoomLevel = 16.0f;
    public String email;
    public String namaUser;
    private String token;
    private String url;
    public int ido;

    Gson gson;
    TextView nm;
    TextView jnsSmph;
    TextView hrg;
    TextView mdSmph;
    Button btnOrder;
    RelativeLayout tlTampil;
    private SharedPreferences.Editor editor;
    List<ListPengambil> list_data = new ArrayList<>();
    View view;
    Location myLocation;

    public HalamanPengambilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_halaman_pengambil, container, false);
        gson = new Gson();
        myLocation = MLocation.getLocation(getContext());
        tlTampil = (RelativeLayout) view.findViewById(R.id.layoutPopup);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String getUserData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_USER_DATA, null);
        String getToken = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (getUserData != null) {
            JSONObject json = null;
            try {
                json = new JSONObject(getUserData);
                namaUser = json.getString("ro_nama_pengguna");
                email = json.getString("ro_email");
                //Log.d("id adalah: ",email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getToken != null) {
            token = getToken;
        }

        nm = (TextView) view.findViewById(R.id.tv_nama_pembuang);
        jnsSmph = (TextView) view.findViewById(R.id.tv_jns_sampah);
        hrg = (TextView) view.findViewById(R.id.tv_harga);
        mdSmph = (TextView) view.findViewById(R.id.tv_mode_sampah);
        btnOrder = (Button) view.findViewById(R.id.btnOrder);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getDataPosisi();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location myLocation = MLocation.getLocation(getContext());
        // Add a marker in Jakarta and move the camera
        LatLng jakarta = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(jakarta).title("Marker in Jakarta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, zoomLevel));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void getDataPosisi(){
        url = Constant.ENDPOINT_GET_ORDER;
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String getLocalData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (getLocalData!=null) {
            token = getLocalData;
        }
        StringRequest req = new StringRequest(Request.Method.GET, url, successListener(), errListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        AppsController.getInstance().addToRequestQueue(req);
    }

    private Response.ErrorListener errListener() {
        return new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.e("Error", "Error get view booking");
                Log.e("Error", String.valueOf(error));
            }
        };
    }
    private Response.Listener<String> successListener(){
        return new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d("respone",response);
                try{
                    JSONObject json = new JSONObject(response);
                    if (json.getInt("status") == 200) {
                        String jsonOutput = json.getString("data");
                        Type listType = new TypeToken<List<ListPengambil>>(){}.getType();
                        list_data = (List<ListPengambil>) gson.fromJson(jsonOutput, listType);

                    } else {
                        Toast.makeText(getContext(),"Get Failed",Toast.LENGTH_LONG).show();
                    }
                    putMarker();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void putMarker(){
        mMap.clear();
        Log.d("List data: ",list_data.toString());
        for(int i = 0; i<list_data.size(); i++){
            LatLng newMarker = new LatLng(list_data.get(i).getLat(), list_data.get(i).getLang());
            mMap.addMarker(new MarkerOptions().position(newMarker).title(list_data.get(i).getAlamat())).setTag(i);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    int x = (int) marker.getTag();
                    ido = x;
                    final ListPengambil dataBaru = list_data.get(x);
                    tlTampil.setVisibility(View.VISIBLE);
                    nm.setText(dataBaru.getNama().toString(), TextView.BufferType.EDITABLE);
                    jnsSmph.setText(dataBaru.getJenisSp().toString(), TextView.BufferType.EDITABLE);
                    hrg.setText(String.valueOf(dataBaru.getHarga()), TextView.BufferType.EDITABLE);
                    mdSmph.setText(dataBaru.getModePb().toString(), TextView.BufferType.EDITABLE);
                    btnOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            post_update_order();
                            myLocation = MLocation.getLocation(getContext());
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr="+
                                            String.valueOf(myLocation.getLatitude())+","+String.valueOf(myLocation.getLongitude())+
                                            "&daddr="+String.valueOf(dataBaru.getLat())+","+String.valueOf(dataBaru.getLang())));
                            startActivity(intent);
                        }
                    });

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
    }

    public void post_update_order(){
        String link = Constant.ENDPOINT_UPDATE_ORDER+ido;
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String getLocalData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (getLocalData!=null) {
            token = getLocalData;
        }
        StringRequest req = new StringRequest(Request.Method.POST, link, listenerSuccess(), listenerErr()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        AppsController.getInstance().addToRequestQueue(req);
    }

    private Response.ErrorListener listenerErr() {
        return new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login");
                Log.e("Error", String.valueOf(error));
            }
        };
    }

    private Response.Listener<String> listenerSuccess() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("response", response);
                    JSONObject json = new JSONObject(response);
                    String message = json.getString("message");
                    String status = json.getString("status");
                    Log.d("message", message);
                    if (status.equals("200"))
                    {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getContext(), Login.class);
//                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
