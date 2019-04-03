package charge.pluggedin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.Toast;


public class BackgroundService extends Service {

    private int sound_file = R.raw.logan_paul_ahh_sound_effect;
    private MediaPlayer media_player;
    private boolean previous_state;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        media_player = MediaPlayer.create(this, sound_file);
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy()
    {
        previous_state = true;
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        previous_state = false;
        createBroadcastReceiver();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Listens for the charging state to change.
     * If the phone begins to draw AC power, then it is plugged in.
     * Therefore, play the audio.
     */
    private void createBroadcastReceiver()
    {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                if(plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB) // Charging
                {
                    // Only play if the previous state was unplugged -->
                    // prevents sound from playing when the app starts
                    if(previous_state == false)
                    {
                        media_player.start();
                        previous_state = true;
                        Toast.makeText(BackgroundService.this.getApplicationContext(), "Charging", Toast.LENGTH_SHORT).show();
                    }
                }
                else // Not charging
                {
                    previous_state = false;
                    Toast.makeText(BackgroundService.this.getApplicationContext(), "Not Charging", Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
    }
}