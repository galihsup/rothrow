package rot.user.tekno.com.rothrow.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esafirm.imagepicker.features.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rot.user.tekno.com.rothrow.AppsController;
import rot.user.tekno.com.rothrow.HalamanUtamaActivity;
import rot.user.tekno.com.rothrow.R;
import rot.user.tekno.com.rothrow.util.Constant;
import rot.user.tekno.com.rothrow.util.MLocation;
import rot.user.tekno.com.rothrow.util.Screen;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class HalamanUtamaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    float zoomLevel = 15.0f;
    private String email;
    private String namaUser;
    private String token;
    public  String idus;
    public double lt;
    public double lg;
    ImageView iv_foto;
    ImageView iv_ambil_foto;
    LinearLayout lv_nampil_foto;
    EditText etHarga;
    TextInputEditText spMode;
    LinearLayout tlTampil;
    View view;
    Button btnSave;
    SearchView search;
    String searchResult;
    private SharedPreferences.Editor editor;
    TextInputEditText spjenis;
    static final Integer LOCATION = 0x1;
    static final Integer CAMERAS = 0x2;
    String foto;
    CharSequence jenis[] = {
            "Sampah Kering",
            "Sampah Basah"
    };

    CharSequence modeSampah[] = {
            "Sharing Fee",
            "Harga Sudah Ditentukan",
            "Pembuang Menentukan Harga",
            "Free"
    };

    public HalamanUtamaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_halaman_utama, container, false);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);

        spjenis = (TextInputEditText)view.findViewById(R.id.et_pilih_jns_sampah);
        spMode = (TextInputEditText)view.findViewById(R.id.et_pilih_modesampah);
        etHarga = (EditText) view.findViewById(R.id.JnsSampahTxt);
        btnSave = (Button) view.findViewById(R.id.btn_order);
        tlTampil = (LinearLayout) view.findViewById(R.id.layoutPU);
        iv_foto = (ImageView) view.findViewById(R.id.iv_foto_sampah);
        iv_ambil_foto = (ImageView) view.findViewById(R.id.iv_ambil_foto);
        lv_nampil_foto = (LinearLayout) view.findViewById(R.id.lv_nampil_foto);
        searchResult = "";
        foto = "";
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPrefs.edit();
        String getUserData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_USER_DATA, null);
        String getToken = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (getUserData != null) {
            JSONObject json = null;
            try {
                json = new JSONObject(getUserData);
                idus = json.getString("id");
                namaUser = json.getString("ro_nama_pengguna");
                email = json.getString("ro_email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getToken != null) {
            token = getToken;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final LinearLayout llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        final ViewGroup.LayoutParams params = llSearch.getLayoutParams();



        search = (SearchView) view.findViewById(R.id.svCari);
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                llSearch.setLayoutParams(params);
                return false;
            }
        });
        //String cr = search.getQuery().toString();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                params.width = Screen.getWidth(getContext()) - 64;
                llSearch.setLayoutParams(params);

                String location = query.toString();
                List<Address> addressList = null;

                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!location.equals("") && addressList != null) {
                    searchResult = location;
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    lt = address.getLatitude();
                    lg = address.getLongitude();
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(query));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            tlTampil.setVisibility(View.VISIBLE);
                            return true;
                        }
                    });
                    spjenis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            etHarga.setVisibility(View.VISIBLE);
                            spMode.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("-- Pilih Jenis Sampah --");
                            builder.setItems(jenis, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    spjenis.setText(jenis[i]);
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                    spMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("-- Pilih Mode Pembuangan --");
                            builder.setItems(modeSampah, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    spMode.setText(modeSampah[i]);
                                    if(modeSampah[i].equals("Free")){
                                        etHarga.setText("0");
                                        etHarga.setEnabled(false);
                                    } else {
                                        etHarga.setEnabled(true);
                                    }
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            lv_nampil_foto.setVisibility(View.VISIBLE);
                        }
                    });
                    lv_nampil_foto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFotoSampah(12);
                            iv_foto.setVisibility(View.VISIBLE);
                        }
                    });
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            tlTampil.setVisibility(View.GONE);
                            lv_nampil_foto.setVisibility(View.GONE);
                            spMode.setVisibility(View.GONE);
                            etHarga.setVisibility(View.GONE);
                            iv_foto.setVisibility(View.GONE);
                        }
                    });
                }
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload();
//                        getDataPosisi();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spMode.setVisibility(View.GONE);
                etHarga.setVisibility(View.GONE);
                tlTampil.setVisibility(View.GONE);
            }
        });
        //getDataPosisi();

        return view;
    }

    public void pindahLokasi(){
        Location myLocation = MLocation.getLocation(getContext());

        // Add a marker in Jakarta and move the camera
        LatLng jakarta = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        //LatLng jakarta = new LatLng(-6.252887, 106.8469626);
        //mMap.addMarker(new MarkerOptions().position(jakarta).title("Marker in Jakarta"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, zoomLevel));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            LatLng jakarta = new LatLng(-6.252887, 106.8469626);
            //mMap.addMarker(new MarkerOptions().position(jakarta).title("Marker in Jakarta"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, zoomLevel));
            mMap.getUiSettings().setZoomControlsEnabled(true);

            return;
        }
        else{
            Location myLocation = MLocation.getLocation(getContext());

            // Add a marker in Jakarta and move the camera
            LatLng jakarta = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            //LatLng jakarta = new LatLng(-6.252887, 106.8469626);
            //mMap.addMarker(new MarkerOptions().position(jakarta).title("Marker in Jakarta"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, zoomLevel));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
            /*if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }*/
            mMap.setMyLocationEnabled(true);
        }

    }

    private void upload() {
        String urlHal = Constant.ENDPOINT_INSERT_ORDER;
        Log.d("link ni: ", urlHal);
        StringRequest req = new StringRequest(Request.Method.POST, urlHal, successListener(), errListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idus);
                params.put("name", namaUser);
                params.put("jenis", spjenis.getText().toString());
                params.put("mode", spMode.getText().toString());
                params.put("alamat", searchResult);
                params.put("lat", String.valueOf(lt));
                params.put("lang", String.valueOf(lg));
                params.put("harga", etHarga.getText().toString());
                Log.d("harga", etHarga.getText().toString());
                params.put("status", "Waiting");
                params.put("foto", foto);
                return params;
            }
        };
//        StringRequest req = new StringRequest(Request.Method.POST, urlHal, successListener(), errListener()){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", "1");
//                params.put("name", "galih");
//                params.put("jenis", "spjenis.getText().toString()");
//                params.put("mode", "spMode.getText().toString()");
//                params.put("alamat", "search.getQuery().toString()");
//                params.put("lat", "String.valueOf(lt)");
//                params.put("lang", "String.valueOf(lg)");
//                params.put("harga", "etHarga.getText().toString()");
//                params.put("status", "Waiting");
//                params.put("foto", "asfdfg");
//                return params;
//            }
//        };

        AppsController.getInstance().addToRequestQueue(req);
    }

    private Response.ErrorListener errListener() {
        return new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login");
                Log.e("Error", String.valueOf(error));
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        };
    }

    private Response.Listener<String> successListener() {
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
                        Toast.makeText(getContext(),"Order Sukses",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), HalamanUtamaActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getFotoSampah(int i) {
        ImagePicker.create(this) // Activity or Fragment
                .single().showCamera(true)
                .start(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && null != data) {

            File file = new File(ImagePicker.getImages(data).get(0).getPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }

            iv_foto.setImageBitmap(rotatedBitmap);
            iv_foto.setMaxHeight(bitmap.getHeight());
            iv_foto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv_foto.setPadding(0,0,0,0);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            foto = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("base", foto);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this.getActivity(), new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(Splash.this, "permission is already granted.", Toast.LENGTH_SHORT).show();
            Log.d("Permission : ","permission is already granted.");
        }
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    if (grantResults.length > 0) {

                        boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        //boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (locationAccepted){
                            Toast.makeText(getContext(),"OK Permission Granted, Now you can access location data."
                                    ,Toast.LENGTH_LONG).show();
                            //pindahLokasi();
                        }
                        else {
                            Toast.makeText(getContext(),"Permission Denied, You cannot access location data."
                                    ,Toast.LENGTH_LONG).show();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                    showMessageOKCancel("You need to allow access to the permissions",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                                                    }
                                                }
                                            });
                                    return;
                                }
                            }

                        }
                    }
                    break;
                case 2:
                    if (grantResults.length > 0) {

                        //boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (cameraAccepted){
                            Toast.makeText(getContext(),"Permission Granted, Now you can access camera.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(),"Permission Denied, You cannot access camera.",Toast.LENGTH_LONG).show();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (shouldShowRequestPermissionRationale(CAMERA)) {
                                    showMessageOKCancel("You need to allow access to the permissions",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        askForPermission(Manifest.permission_group.CAMERA,CAMERAS);
                                                    }
                                                }
                                            });
                                    return;
                                }
                            }

                        }
                    }

                    break;
            }

            //Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            //pindahLokasi();
            Location myLocation = MLocation.getLocation(getContext());
            LatLng jakarta = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, zoomLevel));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
        }else{
            //Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            showMessageOKCancel("You need to allow access to the permissions",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                            }
                        }
                    });
            return;
        }
    }

}
