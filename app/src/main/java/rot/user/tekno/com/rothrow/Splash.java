package rot.user.tekno.com.rothrow;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import rot.user.tekno.com.rothrow.util.Constant;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;

public class Splash extends AppCompatActivity {

    private String statusLogin = "0";
    private String role = "0";
    static final Integer LOCATION = 0x1;
    static final Integer CAMERAS = 0x2;
    Intent intent;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        //getSupportActionBar().hide();

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Splash.this);

        /**ambil data dari shared preference**/
        String getStatusLogin = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_LOGIN_STATUS, null);
        if(getStatusLogin != null){
            statusLogin = "1";
            String getLocalData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_USER_DATA,null);
            if(getLocalData != null){
                JSONObject json = null;
                try{
                    json = new JSONObject(getLocalData);
                    role = json.getString("ro_id_role");
                } catch (JSONException e){

                }
            }
        }
        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{

                    sleep(3000);
                    if(statusLogin.equals("1") && role.equals("1")){
                        intent = new Intent(Splash.this, HalamanUtamaActivity.class);
                    }
                    else if(statusLogin.equals("1") && role.equals("2")){
                        intent = new Intent(Splash.this, HalamanPengambilActivity.class);
                    } else {
//                        askForPermission(Manifest.permission_group.CAMERA,CAMERAS);
                        intent = new Intent(Splash.this, Login.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
//
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Splash.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Splash.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Splash.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(Splash.this, "permission is already granted.", Toast.LENGTH_SHORT).show();
            Log.d("Permission : ","permission is already granted.");
        }
    }

    private void cekStatusPermission(){
        if (ActivityCompat.checkSelfPermission(Splash.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission_group.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//                            askForPermission(ACCESS_FINE_LOCATION,LOCATION);
//                            askForPermission(Manifest.permission.CAMERA,CAMERAS);

            if (!checkPermission()) {
                requestPermission();
            }
            return;
        } else {

        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Splash.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    if (grantResults.length > 0) {

                        boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        //boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (locationAccepted){
                            Toast.makeText(Splash.this,"Permission Granted, Now you can access location data.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Splash.this,"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

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
                            Toast.makeText(Splash.this,"Permission Granted, Now you can access camera.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Splash.this,"Permission Denied, You cannot access camera.",Toast.LENGTH_LONG).show();

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

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
