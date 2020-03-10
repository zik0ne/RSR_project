package androidstudioapp.android.com.rsr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnableGpsWindow extends AppCompatActivity {

    private Button aanzetten;
    private Button annuleren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_gps_window);

        //finds button id and by pressing aanzetten button prompts user to go and enable Gps
        aanzetten = findViewById(R.id.button_aanzetten);
        aanzetten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
                finish();
            }
        });

        //finds button id and by pressing annuleren button returns to MainActivity
        annuleren = findViewById(R.id.button_annuleren);
        annuleren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity();
            }
        });

    }

    //Goes to MainActivity
    public void mainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}