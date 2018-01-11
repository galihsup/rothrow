package rot.user.tekno.com.rothrow;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rot.user.tekno.com.rothrow.util.Constant;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;

public class Login extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPasswordView;
    private SharedPreferences.Editor editor;
    private String yourToken;
    TextView tv_bijak;
    ImageView iv_show_pass;
    Intent intent;

    static final Integer LOCATION = 0x1;
    static final Integer CAMERAS = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        String getUserData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_USER_DATA, null);
        String getToken = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (getUserData != null && getToken !=null) {
            JSONObject json = null;
            try {
                json = new JSONObject(getUserData);
                String role = json.getString("role");
                if (role.equals("2")) {
                    startActivity(new Intent(Login.this, HalamanPengambilActivity.class));
                } else {
                    startActivity(new Intent(Login.this, HalamanUtamaActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        tv_bijak = (TextView) findViewById(R.id.tv_kebijakan);
        tv_bijak.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        mUsername = (EditText) findViewById(R.id.tUsername);
        mPasswordView = (EditText) findViewById(R.id.tPassword);
        TextView tvSignUp = (TextView) findViewById(R.id.daftar_user);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistrasiActivity.class);
                startActivity(intent);
            }
        });
        iv_show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPasswordView.getTransformationMethod() != null){
                    mPasswordView.setTransformationMethod(null);
                } else {
                    mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProses();
            }
        });
    }
    private void loginProses(){
        String url = Constant.ENDPOINT_LOGIN;
        final String username = mUsername.getText().toString();
        final String password = mPasswordView.getText().toString();
        StringRequest req = new StringRequest(Request.Method.POST, url, loginListener(), errListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", username);
                params.put("password", password);
                params.put("login_type", "1");

                return params;
            }
        };
        AppsController.getInstance().addToRequestQueue(req);
    }

    private Response.ErrorListener errListener() {
        return new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login");
                Log.e("Error", String.valueOf(error));
            }
        };
    }

    private Response.Listener<String> loginListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("response", response);
                    JSONObject json = new JSONObject(response);
                    String message = json.getString("message");

                    Log.d("message", message);
                    Toast.makeText(Login.this,message,Toast.LENGTH_LONG).show();

                    if (json.getString("status").equals("200")) {
                        JSONObject data = new JSONObject(json.getString("data"));
                        String role = data.getString("ro_id_role");

                        /** Store data in shared preference **/
                        editor.putString(Constant.KEY_SHAREDPREFS_USER_DATA, json.getString("data"));
                        editor.putString(Constant.KEY_SHAREDPREFS_LOGIN_STATUS, "1");
                        editor.putString(Constant.KEY_SHAREDPREFS_TOKEN, json.getString("_token")); //buat ngambil json token
                        editor.commit();


                        if (role.equals("2")) {
                            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                            intent = new Intent(Login.this, HalamanPengambilActivity.class);

                        } else {
                            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                            intent = new Intent(Login.this, HalamanUtamaActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Login.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(Splash.this, "permission is already granted.", Toast.LENGTH_SHORT).show();
            Log.d("Permission : ","permission is already granted.");
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Login.this)
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
                            Toast.makeText(Login.this,"Permission Granted, Now you can access location data.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Login.this,"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

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
                            Toast.makeText(Login.this,"Permission Granted, Now you can access camera.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Login.this,"Permission Denied, You cannot access camera.",Toast.LENGTH_LONG).show();

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
