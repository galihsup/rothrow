package rot.user.tekno.com.rothrow;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rot.user.tekno.com.rothrow.model.ListPengambil;
import rot.user.tekno.com.rothrow.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView rvSiswa;
    private HistoryAdapter historyAdapter;
    private List<ListPengambil> historyModel;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rvSiswa = (RecyclerView) view.findViewById(R.id.rv_siswa);
        rvSiswa.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyModel = new ArrayList<>();
        historyAdapter = new HistoryAdapter(getContext(), historyModel);
        rvSiswa.setAdapter(historyAdapter);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String getUserData = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_USER_DATA, null);
        if (getUserData != null) {
            JSONObject json = null;
            try {
                json = new JSONObject(getUserData);
                String id = json.getString("id");
                getDataPosisi(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public void getDataPosisi(String id) {
        String url = Constant.ENDPOINT_GET_ORDER + "/" + id ;
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String token = sharedPrefs.getString(Constant.KEY_SHAREDPREFS_TOKEN, null);
        if (token != null) {

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
                try{
                    JSONObject json = new JSONObject(response);
                    if (json.getInt("status") == 200) {
                        String jsonOutput = json.getString("data");
                        Log.d("json_out", jsonOutput);
                        Type listType = new TypeToken<List<ListPengambil>>(){}.getType();
                        historyModel = (List<ListPengambil>) new Gson().fromJson(jsonOutput, listType);
                        historyAdapter = new HistoryAdapter(getContext(), historyModel);
                        rvSiswa.setAdapter(historyAdapter);
                    } else {
                        //callback.onFinish(false, "failed");
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
