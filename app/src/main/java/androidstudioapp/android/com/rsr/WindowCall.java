package androidstudioapp.android.com.rsr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class WindowCall extends AppCompatActivity {

    private Button button_Belnu;
    private Button button_Annuleren;
    private static final int REQUEST_CALL = 1;
    public boolean permission;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_window_call);


        //Pressing the button Annuleren to close the window
        button_Annuleren = findViewById(R.id.button_x_Annuleren);
        button_Annuleren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Pressing the button Belnu and start calling with the help of startCall()
        button_Belnu = findViewById(R.id.button_Belnu);
        button_Belnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCall();
            }
        });


    }


    //Asks for call permissions
    public void askpermission() {
        ActivityCompat.requestPermissions(WindowCall.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        if (ContextCompat.checkSelfPermission(WindowCall.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

        } else
            permission = true;


    }


    //Results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }


    }


    //Making call to number +319007788990
    public void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+31900778890"));

        if (!permission) {
            askpermission();
        } else {
            startActivity(callIntent);
        }
    }

}
