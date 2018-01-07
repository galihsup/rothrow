package rot.user.tekno.com.rothrow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPasswordView;
    private SharedPreferences.Editor editor;
    private String yourToken;
    TextView tv_bijak;
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

                        Intent intent;
                        if (role.equals("2")) {
                            intent = new Intent(Login.this, HalamanPengambilActivity.class);
                        } else {
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
}
