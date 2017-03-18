package maderski.bluetoothautoplaymusic.Controls;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import maderski.bluetoothautoplaymusic.SharedPrefs.BAPMDataPreferences;
import maderski.bluetoothautoplaymusic.SharedPrefs.BAPMPreferences;

/**
 * Created by Jason on 4/2/16.
 */
public class VolumeControl {

    private static final String TAG = VolumeControl.class.getName();

    private AudioManager am;
    private Context mContext;

    public VolumeControl(Context context){
        am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mContext = context;
    }

    //Set Mediavolume to MAX
    public void volumeMAX(){
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "Max Media Volume is: " + Integer.toString(maxVolume));
        am.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
    }

    public void saveOriginalVolume(){
        int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        BAPMDataPreferences.setOriginalMediaVolume(mContext, originalVolume);
    }

    //Set original media volume
    public void setToOriginalVolume(){
        int originalMediaVolume = BAPMDataPreferences.getOriginalMediaVolume(mContext);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalMediaVolume, 0);

        Log.d(TAG, "Media Volume is set to: " + Integer.toString(originalMediaVolume));
    }

    //Wait 3 seconds before getting the Original Volume
    public void delayGetOrigVol(int seconds){
        int milliseconds = seconds * 1000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BAPMDataPreferences.setOriginalMediaVolume(mContext, am.getStreamVolume(AudioManager.STREAM_MUSIC));

                Log.d(TAG, "Original Media Volume is: " + Integer.toString(BAPMDataPreferences.getOriginalMediaVolume(mContext)));

                Intent launchIntent = new Intent();
                launchIntent.setAction("maderski.bluetoothautoplaymusic.offtelephonelaunch");
                mContext.sendBroadcast(launchIntent);
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }

    private void setToMaxVol(){
        final int maxVolume = BAPMPreferences.getUserSetMaxVolume(mContext);
        if (am.getStreamVolume(AudioManager.STREAM_MUSIC) != maxVolume) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            Log.d(TAG, "Set Volume To MAX");
        } else if (am.getStreamVolume(AudioManager.STREAM_MUSIC) == maxVolume) {
            Log.d(TAG, "Volume is at MAX!");
        }
    }

    public void checkSetMAXVol(int seconds){
        int milliseconds = seconds * 1000;

        setToMaxVol();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setToMaxVol();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }
}