package maderski.bluetoothautoplaymusic.Helpers;

import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.Set;

import maderski.bluetoothautoplaymusic.Analytics.FirebaseHelper;
import maderski.bluetoothautoplaymusic.BluetoothActions.BTConnectActions;
import maderski.bluetoothautoplaymusic.BluetoothActions.BTDisconnectActions;
import maderski.bluetoothautoplaymusic.BuildConfig;
import maderski.bluetoothautoplaymusic.Controls.PlayMusicControl;
import maderski.bluetoothautoplaymusic.Controls.VolumeControl;
import maderski.bluetoothautoplaymusic.Notification;
import maderski.bluetoothautoplaymusic.Receivers.BTStateChangedReceiver;
import maderski.bluetoothautoplaymusic.Receivers.CustomReceiver;
import maderski.bluetoothautoplaymusic.Receivers.PowerReceiver;
import maderski.bluetoothautoplaymusic.SharedPrefs.BAPMDataPreferences;
import maderski.bluetoothautoplaymusic.SharedPrefs.BAPMPreferences;

/**
 * Created by Jason on 6/1/17.
 */

public class BluetoothLaunchHelper {
    private static final String TAG = "BluetoothLaunchHelper";

    private final Context mContext;
    private final String mDeviceName;
    private final int mState;

    public BluetoothLaunchHelper(Context context, String btDeviceName, int state){
        mContext = context;
        mDeviceName = btDeviceName;
        mState = state;
    }

    public void a2dpAction() {
        boolean isHeadphones = BAPMDataPreferences.getIsAHeadphonesDevice(mContext);

        if(isHeadphones){
            BAPMDataPreferences.setIsHeadphonesDevice(mContext, false);
        }

        switch (mState) {
            case BluetoothProfile.STATE_CONNECTING:
                Log.d(TAG, "A2DP CONNECTING");

                // Get Original volume
                VolumeControl volumeControl = new VolumeControl(mContext);
                volumeControl.saveOriginalVolume();
                Log.i(TAG, "Original Media Volume is: " + Integer.toString(BAPMDataPreferences.getOriginalMediaVolume(mContext)));

                checkForWifiTurnOffDevice(true);

                startReceivers();
                break;
            case BluetoothProfile.STATE_CONNECTED:
                Log.d(TAG, "A2DP CONNECTED");

                checksBeforeLaunch();

                FirebaseHelper firebaseHelper = new FirebaseHelper(mContext);
                firebaseHelper.connectViaA2DP(mDeviceName, true);
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                Log.d(TAG, "A2DP DISCONNECTING");
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                Log.d(TAG, "A2DP DISCONNECTED");

                if(BuildConfig.DEBUG) {
                    Log.i(TAG, "Device disconnected: " + mDeviceName);
                    Log.i(TAG, "Ran actionOnBTConnect: " + Boolean.toString(BAPMDataPreferences.getRanActionsOnBtConnect(mContext)));
                    Log.i(TAG, "LaunchNotifPresent: " + Boolean.toString(BAPMDataPreferences.getLaunchNotifPresent(mContext)));
                }

                if(BAPMDataPreferences.getRanActionsOnBtConnect(mContext)) {
                    PlayMusicControl.cancelCheckIfPlaying();
                    BTDisconnectActions btDisconnectActions = new BTDisconnectActions(mContext);
                    btDisconnectActions.actionsOnBTDisconnect();
                }

                if(BAPMPreferences.getWaitTillOffPhone(mContext) && BAPMDataPreferences.getLaunchNotifPresent(mContext)){
                    Notification notification = new Notification();
                    notification.removeBAPMMessage(mContext);
                }

                if(!BAPMDataPreferences.getRanActionsOnBtConnect(mContext)){
                    checkForWifiTurnOffDevice(false);
                }

                stopReceivers();
                break;
        }
    }

    private void startReceivers(){
        ReceiverHelper.startReceiver(mContext, BTStateChangedReceiver.class);

        if(BAPMPreferences.getWaitTillOffPhone(mContext) || BAPMPreferences.getPowerConnected(mContext)){
            ReceiverHelper.startReceiver(mContext, CustomReceiver.class);
        }

        if(BAPMPreferences.getPowerConnected(mContext)) {
            ReceiverHelper.startReceiver(mContext, PowerReceiver.class);
        }
    }

    private void stopReceivers(){
        ReceiverHelper.stopReceiver(mContext, BTStateChangedReceiver.class);

        if(BAPMPreferences.getPowerConnected(mContext)) {
            ReceiverHelper.stopReceiver(mContext, PowerReceiver.class);
        }

        if(BAPMPreferences.getWaitTillOffPhone(mContext) || BAPMPreferences.getPowerConnected(mContext)){
            ReceiverHelper.stopReceiver(mContext, CustomReceiver.class);
        }
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
