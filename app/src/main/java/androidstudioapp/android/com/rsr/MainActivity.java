package androidstudioapp.android.com.rsr;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    ActionBar app_bar;
    private Button button;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_main_tablet);
        }
        else
        {
            setContentView(R.layout.activity_main);
        }


        //commands to reveal ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        this.setSupportActionBar(toolbar);

        //set Toolbar tittle
        this.getSupportActionBar().setTitle("RSR Revelidatieservice");


        //Finds button id and moves from one activity to another by clicking button RSR Pecchulp
        button = findViewById(R.id.button_RSRPechhulp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });

    }

    //Getting the id from res-menu-main in order to start the info button activity
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.info_button:
                startInfoButton();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }
    // Inflate the menu; this adds items to the action bar.
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Pop up maps activity
    public void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    //Pop up info window activity
    public void startInfoButton(){
        Intent intent = new Intent(this,InfoOverRSR.class);
        startActivity(intent);

    }

}





