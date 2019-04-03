package charge.pluggedin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class HomeActivity extends AppCompatActivity {

    private CheckBox disable_service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(HomeActivity.this, BackgroundService.class));
        allowServiceDisable();
    }

    private void allowServiceDisable()
    {
        disable_service = (CheckBox) findViewById(R.id.disable_app_checkbox);
        disable_service.setChecked(false);  // App should be enabled on startup
        disable_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(!isChecked)  // App enabled
                {
                    startService(new Intent(HomeActivity.this, BackgroundService.class));
                }
                else    // Disabled
                {
                    stopService(new Intent(HomeActivity.this, BackgroundService.class));
                }
            }
        });
    }



}
