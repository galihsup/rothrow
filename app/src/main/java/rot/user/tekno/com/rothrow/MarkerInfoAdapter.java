package rot.user.tekno.com.rothrow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Galih on 12/30/2017.
 */

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public MarkerInfoAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.map_marker_info, null);

        LatLng latLng = arg0.getPosition();
        return v;
    }
}
