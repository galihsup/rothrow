package rot.user.tekno.com.rothrow;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Galih on 1/2/2018.
 */

public class API {
    public static void POST(String url, final VolleyCallback callback, final Map<String, String> params) {
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getInt("status") == 200) {
                                callback.onFinish(true, json.getString("data"));
                            } else {
                                callback.onFinish(false, "failed");
//                                callback.onFinish(false, json.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFinish(false, error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params == null)
                    return super.getParams();
                else
                    return params;
            }
        };
        AppsController.getInstance().addToRequestQueue(req);
    }

    public static void GET(String url, final VolleyCallback callback) {
        StringRequest req = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Respon API : ",response);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getInt("status") == 200) {
                            callback.onFinish(true, json.getString("data"));
                        } else {
                            callback.onFinish(false, "failed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFinish(false, error.toString());
                }
            });
        AppsController.getInstance().addToRequestQueue(req);
    }

    public interface VolleyCallback {
        void onFinish(Boolean status, String result);
    }
}
