package rot.user.tekno.com.rothrow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import rot.user.tekno.com.rothrow.util.Constant;

public class SplashActivity extends AppCompatActivity {

    private String statusLogin = "0";
    private String role = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

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
                    Intent intent;
                    sleep(3000);
                    if(statusLogin.equals("1") && role.equals("1")){
                        intent = new Intent(SplashActivity.this, HalamanUtamaActivity.class);
                    }
                    else if(statusLogin.equals("1") && role.equals("2")){
                        intent = new Intent(SplashActivity.this, HalamanPengambilActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, Login.class);
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
}
