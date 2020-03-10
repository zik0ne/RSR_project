package androidstudioapp.android.com.rsr;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoOverRSR extends Activity {

    Button button;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_info_over_rsr_tablet);

        }else {
            setContentView(R.layout.activity_info_over_rsr);
        }


        //Close the pop up window by pressing BEVESTIG button
        button = findViewById(R.id.button_bevestig);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Finds the url_text id and makes the text clickable. In styles folder you set up the word you want to link with a website
        textView = findViewById(R.id.url_text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


    }
}
