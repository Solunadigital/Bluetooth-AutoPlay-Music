package maderski.bluetoothautoplaymusic.helpers;

import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Set;

import maderski.bluetoothautoplaymusic.analytics.FirebaseHelper;
import maderski.bluetoothautoplaymusic.bluetoothactions.BTConnectActions;
import maderski.bluetoothautoplaymusic.BuildConfig;
import maderski.bluetoothautoplaymusic.controls.PlayMusicControl;
import maderski.bluetoothautoplaymusic.controls.VolumeControl;
import maderski.bluetoothautoplaymusic.Notification;
import maderski.bluetoothautoplaymusic.services.BTStateChangedService;
import maderski.bluetoothautoplaymusic.services.BTDisconnectService;
import maderski.bluetoothautoplaymusic.services.OnBTConnectService;
import maderski.bluetoothautoplaymusic.sharedprefs.BAPMDataPreferences;
import maderski.bluetoothautoplaymusic.sharedprefs.BAPMPreferences;
import maderski.bluetoothautoplaymusic.utils.ServiceUtils;

/**
 * Created by Jason on 6/1/17.
 */

public class BluetoothConnectHelper {
    private static final String TAG = "BluetoothLaunchHelper";

    private final Context mContext;
    private final String mDeviceName;

    public BluetoothConnectHelper(Context context, String btDeviceName){
        mContext = context;
        mDeviceName = btDeviceName;
    }

    public void a2dpActions(int state) {
        boolean isHeadphones = BAPMDataPreferences.getIsAHeadphonesDevice(mContext);

        if(isHeadphones){
            BAPMDataPreferences.setIsHeadphonesDevice(mContext, false);
        }

        switch (state) {
            case BluetoothProfile.STATE_CONNECTING:
                Log.d(TAG, "A2DP CONNECTING");
                break;
            case BluetoothProfile.STATE_CONNECTED:
                Log.d(TAG, "A2DP CONNECTED");

                // Get Original volume
                VolumeControl volumeControl = new VolumeControl(mContext);
                volumeControl.saveOriginalVolume();
                Log.i(TAG, "Original Media Volume is: " + Integer.toString(BAPMDataPreferences.getOriginalMediaVolume(mContext)));

                checkForWifiTurnOffDevice(true);

                startAdditionalServices();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checksBeforeLaunch();

                        FirebaseHelper firebaseHelper = new FirebaseHelper(mContext);
                        firebaseHelper.connectViaA2DP(mDeviceName, true);
                    }
                }, 500);
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                Log.d(TAG, "A2DP DISCONNECTING");
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                Log.d(TAG, "A2DP DISCONNECTED");
                btDisconnectActions();
                break;
        }
    }

    public void btDisconnectActions(){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Device disconnected: " + mDeviceName);
            Log.i(TAG, "Ran actionOnBTConnect: " + Boolean.toString(BAPMDataPreferences.getRanActionsOnBtConnect(mContext)));
            Log.i(TAG, "LaunchNotifPresent: " + Boolean.toString(BAPMDataPreferences.getLaunchNotifPresent(mContext)));
        }

        stopAdditionalServices();

        if(BAPMDataPreferences.getRanActionsOnBtConnect(mContext)) {
            PlayMusicControl.cancelCheckIfPlaying();
            ServiceUtils.startService(mContext, BTDisconnectService.class, BTDisconnectService.TAG);
        }

        if(BAPMPreferences.getWaitTillOffPhone(mContext) && BAPMDataPreferences.getLaunchNotifPresent(mContext)){
            Notification notification = new Notification();
            notification.removeBAPMMessage(mContext);
        }

        if(!BAPMDataPreferences.getRanActionsOnBtConnect(mContext)){
            checkForWifiTurnOffDevice(false);
        }
    }

    private void startAdditionalServices(){
        ServiceUtils.startService(mContext, OnBTConnectService.class, OnBTConnectService.TAG);
        ServiceUtils.startService(mContext, BTStateChangedService.class, BTStateChangedService.TAG);
    }

    private void stopAdditionalServices(){
        boolean didNotLaunchBAPM = !BAPMDataPreferences.getRanActionsOnBtConnect(mContext);
        Log.d(TAG, "Did not launch BAPM: " + String.valueOf(didNotLaunchBAPM));

        if(didNotLaunchBAPM) {
            ServiceUtils.stopService(mContext, OnBTConnectService.class, OnBTConnectService.TAG);
        }

        ServiceUtils.stopService(mContext, BTStateChangedService.class, BTStateChangedService.TAG);
    }

    private void checksBeforeLaunch(){
        boolean powerRequired = BAPMPreferences.getPowerConnected(mContext);

        BTConnectActions btConnectActions = new BTConnectActions(mContext);

        if (powerRequired) {
            if (PowerHelper.isPluggedIn(mContext)) {
                btConnectActions.OnBTConnect();
            }
        } else {
            btConnectActions.OnBTConnect();
        }
    }

    private void checkForWifiTurnOffDevice(boolean isConnected){
        Set<String> turnOffWifiDevices = BAPMPreferences.getTurnWifiOffDevices(mContext);
        if(turnOffWifiDevices.size() > 0) {
            if (turnOffWifiDevices.contains(mDeviceName)) {
                BAPMDataPreferences.setIsTurnOffWifiDevice(mContext, isConnected);
                Log.d(TAG, "TURN OFF WIFI DEVICE SET TO: " + Boolean.toString(isConnected));
            }
        }
    }
}
