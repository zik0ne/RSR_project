package androidstudioapp.android.com.rsr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;

import android.view.View;

import android.widget.TextView;



import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class InfoWindowAdapter extends Activity  implements GoogleMap.InfoWindowAdapter {


    private Context context;

    public InfoWindowAdapter() {
    }

    public InfoWindowAdapter(Context context) {
        this.context = context;
    }


    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_info_window_adapter, null);
        //Set the background color
        v.setBackgroundColor(context.getResources().getColor(R.color.trans_blue));



        LatLng latLng = marker.getPosition();

        //Geocoder takes the coordinates and converts them into Information
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context);

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);


            //find the text id
            TextView headline = v.findViewById(R.id.headline);
            TextView LatLng = (TextView) v.findViewById(R.id.address);
            TextView info = (TextView) v.findViewById(R.id.tv_info);

            //Shows the information we want in the info window adapter screen
            headline.setText("Uw Locatie:");
            LatLng.setText(address);
            info.setText("Onthoud deze locatie voor het telefoongesprek");

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return v;


    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;

    }



}
