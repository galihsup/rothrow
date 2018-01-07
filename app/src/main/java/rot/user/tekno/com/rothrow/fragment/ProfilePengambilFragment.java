package rot.user.tekno.com.rothrow.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import rot.user.tekno.com.rothrow.API;
import rot.user.tekno.com.rothrow.R;
import rot.user.tekno.com.rothrow.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePengambilFragment extends Fragment {
    TextView txtNama;
    TextView txtAlamat;
    TextView txtEmail;
    TextView txtJoin;
    TextView txtStatus;
    TextView txtHp;
    private String url;
    private String id_user;
    public String token;

    public ProfilePengambilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_pengambil, container, false);

        /** Get data from shared preference untuk nyimpen data**/
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
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

        txtNama = (TextView)view.findViewById(R.id.tv_name);
        txtAlamat = (TextView)view.findViewById(R.id.tv_alamat);
        txtEmail = (TextView)view.findViewById(R.id.tv_email);
        txtJoin = (TextView)view.findViewById(R.id.tv_join);
        txtStatus = (TextView)view.findViewById(R.id.tv_verify);
        txtHp = (TextView)view.findViewById(R.id.tv_hp);

        getDataProfile();

        return view;
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
