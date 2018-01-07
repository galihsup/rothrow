package rot.user.tekno.com.rothrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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

public class RegistrasiActivity extends AppCompatActivity {

    Button btn_regis;
    ImageView ivBack;
    private String url;

    EditText etNama;
    RadioButton rdMale;
    RadioButton rdFemale;
    EditText etEmail;
    EditText etPass;
    EditText etNohp;
    EditText etAlamat;
    TextView tvTgl;
    public String rdGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        ivBack = (ImageView)findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_regis = (Button) findViewById(R.id.btn_submit);
        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRegist();
            }
        });
    }

    private void postRegist(){
        etNama = (EditText) findViewById(R.id.txt_nama_pengguna);
        final String nama = etNama.getText().toString();

        rdMale = (RadioButton) findViewById(R.id.rd_male);
        rdFemale = (RadioButton) findViewById(R.id.rd_female);

        if(rdMale.isChecked() == true){
            rdGender = rdMale.getText().toString();
        } else if(rdFemale.isChecked() == true){
            rdGender = rdFemale.getText().toString();
        }

        etEmail = (EditText) findViewById(R.id.txt_email_pengguna);
        final String email = etEmail.getText().toString();

        etPass = (EditText) findViewById(R.id.txt_password_pengguna);
        final String pass = etPass.getText().toString();

        etNohp = (EditText) findViewById(R.id.txt_nohp);
        final String hp = etNohp.getText().toString();

        etAlamat = (EditText) findViewById(R.id.txt_alamat_pengguna);
        final String alamat = etAlamat.getText().toString();

        tvTgl = (TextView) findViewById(R.id.txt_tgl_gabung);
        final String tglGabung = tvTgl.getText().toString();

        url = Constant.ENDPOINT_REGISTER_USER;
        Log.d("link ni: ", url);
        StringRequest req = new StringRequest(Request.Method.POST, url, successListener(), errListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", nama);
                params.put("gender", rdGender);
                params.put("alamat", alamat);
                params.put("email", email);
                params.put("password", pass);
                params.put("tanggal", tglGabung);
                params.put("nohp", hp);

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
                        Intent intent = new Intent(RegistrasiActivity.this, Login.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
