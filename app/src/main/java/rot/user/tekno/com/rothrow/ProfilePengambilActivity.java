package rot.user.tekno.com.rothrow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import rot.user.tekno.com.rothrow.util.Constant;

public class ProfilePengambilActivity extends AppCompatActivity {

    TextView txtNama;
    TextView txtAlamat;
    TextView txtEmail;
    TextView txtJoin;
    TextView txtStatus;
    TextView txtHp;
    ImageView ivBack;
    private String url;
    private String id_user;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pengambil);

        /** Get data from shared preference untuk nyimpen data**/
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String getUserData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_USER_DATA, null);
        String getToken = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (getUserData!=null) {
            JSONObject json = null;
            try {
                json = new JSONObject(getUserData);
                id_user = json.getString("id");
                Log.d("id adalah: ",id_user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getToken!=null) {
            token = getToken;
        }
        Log.d("token", token);

        ivBack = (ImageView)findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtNama = (TextView)findViewById(R.id.tv_name);
        txtAlamat = (TextView)findViewById(R.id.tv_alamat);
        txtEmail = (TextView)findViewById(R.id.tv_email);
        txtJoin = (TextView)findViewById(R.id.tv_join);
        txtStatus = (TextView)findViewById(R.id.tv_verify);
        txtHp = (TextView)findViewById(R.id.tv_hp);

        getDataProfile();
    }

    private void getDataProfile(){
        url = Constant.ENDPOINT_GET_PROFILE+id_user;
        Log.d("link ni: ", url);
        API.VolleyCallback callback = new API.VolleyCallback() {
            @Override
            public void onFinish(Boolean status, String result) {
                Log.d("statusnya: ",status.toString());
                if(status){
                    try {
                        JSONObject json = new JSONObject(result);
                        txtNama.setText(json.getString("ro_nama_pengguna"));
                        txtAlamat.setText(json.getString("ro_alamat"));
                        txtEmail.setText(json.getString("ro_email"));
                        txtJoin.setText("Bergabung Pada "+json.getString("ro_tanggal_join"));
                        txtStatus.setText(json.getString("ro_status"));
                        txtHp.setText(json.getString("ro_no_hp"));

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Resultnya : ", result);
                }
            }
        };
        API.GET(url,callback);
    }
}
