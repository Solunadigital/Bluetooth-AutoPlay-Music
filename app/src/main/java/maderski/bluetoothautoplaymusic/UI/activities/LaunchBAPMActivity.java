package maderski.bluetoothautoplaymusic.UI.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import maderski.bluetoothautoplaymusic.Analytics.FirebaseHelper;
import maderski.bluetoothautoplaymusic.LaunchApp;
import maderski.bluetoothautoplaymusic.R;
import maderski.bluetoothautoplaymusic.Controls.WakeLockControl.ScreenONLock;
import maderski.bluetoothautoplaymusic.SharedPrefs.BAPMPreferences;

public class LaunchBAPMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_bapm);

        // Create Firebase Event
        FirebaseHelper firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.activityLaunched(FirebaseHelper.ActivityName.LAUNCH_BAPM);

        // Dismiss the keyguard
        dismissKeyGuard(LaunchBAPMActivity.this);

        // Hide the fake loading screen.  This is used to keep this activity alive while dismissing the keyguard
        sendHomeAppTimer(3);
    }

    //Dismiss the KeyGuard
    private void dismissKeyGuard(Context context){
        Window window = getWindow();
        if (!BAPMPreferences.getKeepScreenON(context)){
            ScreenONLock screenONLock = ScreenONLock.getInstance();
            screenONLock.enableWakeLock(context);
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            screenONLock.releaseWakeLock();
        }else{
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    private void sendHomeAppTimer(int seconds){
        boolean launchMaps = BAPMPreferences.getLaunchGoogleMaps(this);
        boolean launchPlayer = BAPMPreferences.getLaunchMusicPlayer(this);

        if(!launchMaps && !launchPlayer) {
            final Context context = this;
            int milliSeconds = seconds * 1000;
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    LaunchApp launchApp = new LaunchApp();
                    launchApp.sendEverythingToBackground(context);
                }
            };
            handler.postDelayed(runnable, milliSeconds);
        }
    }
}