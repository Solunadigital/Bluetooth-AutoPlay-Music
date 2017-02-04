package maderski.bluetoothautoplaymusic.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import maderski.bluetoothautoplaymusic.PackageTools;

/**
 * Created by Jason on 1/5/16.
 *
 * Save and read program settings using this class
 */
public class BAPMPreferences {

    private static SharedPreferences.Editor _editor;

    private static String[] setValues = new String[]{"1", "2", "3", "4", "5", "6", "7"};
    private static Set<String> launchDays = new HashSet<>(Arrays.asList(setValues));
    private static String MY_PREFS_NAME = "BAPMPreference";

    //Keys for enabling/disabling things
    private static final String LAUNCH_MAPS_KEY =  "googleMaps";
    private static final String KEEP_SCREEN_ON_KEY = "keepScreenON";
    private static final String PRIORITY_MODE_KEY = "priorityMode";
    private static final String MAX_VOLUME_KEY = "maxVolume";
    private static final String LAUNCH_MUSIC_PLAYER_KEY = "launchMusic";
    private static final String PKG_SELECTED_MUSIC_PLAYER_KEY = "PkgSelectedMusicPlayer";
    private static final String UNLOCK_SCREEN_KEY = "UnlockScreen";
    private static final String BTDEVICES_KEY = "BTDevices";
    private static final String MAPS_CHOICE_KEY = "MapsChoice";
    private static final String FIRST_INSTALL_KEY = "FirstInstall";
    private static final String AUTOPLAY_MUSIC_KEY = "AutoPlayMusic";
    private static final String POWER_CONNECTED_KEY = "PowerConnected";
    private static final String SEND_TO_BACKGROUND_KEY = "SendToBackground";
    private static final String WAIT_TILL_OFF_PHONE_KEY = "WaitTillOffPhone";
    private static final String AUTO_BRIGHTNESS_KEY = "AutoBrightness";
    private static final String DAYS_TO_LAUNCH_MAPS_KEY = "DaysToLaunchMaps";
    private static final String DIM_TIME_KEY = "DimTime";
    private static final String BRIGHT_TIME_KEY = "BrightTime";
    private static final String HEADPHONE_DEVICES_KEY = "HeadphoneDevices";
    private static final String HEADPHONE_PREFERRED_VOLUME_KEY = "HeadphonePreferredVolumeKey";
    private static final String USER_SET_MAX_VOLUME_KEY = "UserSetMaxVolumeKey";
    

    //Writes to SharedPreferences, but still need to commit setting to save it
    private static SharedPreferences.Editor editor(Context context){

        if(_editor == null){
            _editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
            _editor.commit();
        }

        return _editor;
    }

    //Reads SharedPreferences value
    private static SharedPreferences reader(Context context){

        return context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    public static void setUserSetMaxVolume(Context context, int volume){
        editor(context).putInt(USER_SET_MAX_VOLUME_KEY, volume);
        commit(context);
    }

    public static int getUserSetMaxVolume(Context context){
        return reader(context).getInt(USER_SET_MAX_VOLUME_KEY, 15);
    }

    public static void setHeadphonePreferredVolume(Context context, int volume){
        editor(context).putInt(HEADPHONE_PREFERRED_VOLUME_KEY, volume);
        commit(context);
    }

    public static int getHeadphonePreferredVolume(Context context){
        return reader(context).getInt(HEADPHONE_PREFERRED_VOLUME_KEY, 7);
    }

    //Preferences for enabling and disabling things
    public static void setBrightTime(Context context, int time){
        editor(context).putInt(BRIGHT_TIME_KEY, time);
        commit(context);
    }

    public static int getBrightTime(Context context){
        return reader(context).getInt(BRIGHT_TIME_KEY, 700);
    }

    public static void setDimTime(Context context, int time){
        editor(context).putInt(DIM_TIME_KEY, time);
        commit(context);
    }

    public static int getDimTime(Context context){
        return reader(context).getInt(DIM_TIME_KEY, 2000);
    }

    public static void setDaysToLaunchMaps(Context context, Set<String> _stringSet){
        editor(context).putStringSet(DAYS_TO_LAUNCH_MAPS_KEY, _stringSet);
        commit(context);
    }

    public static Set<String> getDaysToLaunchMaps(Context context){
        return reader(context).getStringSet(DAYS_TO_LAUNCH_MAPS_KEY, launchDays);
    }

    public static void setAutoBrightness(Context context, boolean enabled){
        editor(context).putBoolean(AUTO_BRIGHTNESS_KEY, enabled);
        commit(context);
    }

    public static boolean getAutoBrightness(Context context){
        return reader(context).getBoolean(AUTO_BRIGHTNESS_KEY, false);
    }

    public static void setLaunchGoogleMaps(Context context, boolean enabled){
        editor(context).putBoolean(LAUNCH_MAPS_KEY, enabled);
        commit(context);
    }

    public static boolean getLaunchGoogleMaps(Context context){
        return reader(context).getBoolean(LAUNCH_MAPS_KEY, false);
    }

    public static void setKeepScreenON(Context context, boolean enabled){
        editor(context).putBoolean(KEEP_SCREEN_ON_KEY, enabled);
        commit(context);
    }

    public static boolean getKeepScreenON(Context context) {
        return reader(context).getBoolean(KEEP_SCREEN_ON_KEY, false);
    }

    public static void setPriorityMode(Context context, boolean enabled){
        editor(context).putBoolean(PRIORITY_MODE_KEY, enabled);
        commit(context);
    }

    public static boolean getPriorityMode(Context context){
        return reader(context).getBoolean(PRIORITY_MODE_KEY, false);
    }

    public static void setMaxVolume(Context context, boolean enabled){
        editor(context).putBoolean(MAX_VOLUME_KEY, enabled);
        commit(context);
    }

    public static boolean getMaxVolume(Context context){
        return reader(context).getBoolean(MAX_VOLUME_KEY, false);
    }

    public static void setLaunchMusicPlayer(Context context, boolean enabled){
        editor(context).putBoolean(LAUNCH_MUSIC_PLAYER_KEY, enabled);
        commit(context);
    }

    public static boolean getLaunchMusicPlayer(Context context){
        return reader(context).getBoolean(LAUNCH_MUSIC_PLAYER_KEY, false);
    }

    public static void setPkgSelectedMusicPlayer(Context context, String packageName){
        editor(context).putString(PKG_SELECTED_MUSIC_PLAYER_KEY, packageName);
        commit(context);
    }

    public static String getPkgSelectedMusicPlayer(Context context){
        return reader(context).getString(PKG_SELECTED_MUSIC_PLAYER_KEY, PackageTools.GOOGLEPLAYMUSIC);
    }


    public static void setUnlockScreen(Context context, Boolean enabled){
        editor(context).putBoolean(UNLOCK_SCREEN_KEY, enabled);
        commit(context);
    }

    public static Boolean getUnlockScreen(Context context){
        return reader(context).getBoolean(UNLOCK_SCREEN_KEY, false);
    }

    public static void setHeadphoneDevices(Context context, Set<String> headphoneDevices){
        editor(context).putStringSet(HEADPHONE_DEVICES_KEY, headphoneDevices);
        commit(context);
    }

    public static Set<String> getHeadphoneDevices(Context context){
        return reader(context).getStringSet(HEADPHONE_DEVICES_KEY, new HashSet<String>());
    }

    public static void setBTDevices(Context context, Set<String> _stringSet){
        editor(context).putStringSet(BTDEVICES_KEY, _stringSet);
        commit(context);
    }

    public static Set<String> getBTDevices(Context context){
        return reader(context).getStringSet(BTDEVICES_KEY, null);
    }

    public static void setMapsChoice(Context context, String SelectedMapsApp){
        editor(context).putString(MAPS_CHOICE_KEY, SelectedMapsApp);
        commit(context);
    }

    public static String getMapsChoice(Context context){
        return reader(context).getString(MAPS_CHOICE_KEY, "com.google.android.apps.maps");
    }

    public static void setFirstInstall(Context context, boolean isFirstInstall){
        editor(context).putBoolean(FIRST_INSTALL_KEY, isFirstInstall);
        commit(context);
    }

    public static boolean getFirstInstallKey(Context context){
        return reader(context).getBoolean(FIRST_INSTALL_KEY, true);
    }

    public static void setAutoplayMusic(Context context, boolean enabled){
        editor(context).putBoolean(AUTOPLAY_MUSIC_KEY, enabled);
        commit(context);
    }

    public static boolean getAutoPlayMusic(Context context){
        return reader(context).getBoolean(AUTOPLAY_MUSIC_KEY, true);
    }

    public static void setPowerConnected(Context context, boolean enabled){
        editor(context).putBoolean(POWER_CONNECTED_KEY, enabled);
        commit(context);
    }

    public static boolean getPowerConnected(Context context){
        return reader(context).getBoolean(POWER_CONNECTED_KEY, false);
    }

    public static void setSendToBackground(Context context, boolean enabled){
        editor(context).putBoolean(SEND_TO_BACKGROUND_KEY, enabled);
        commit(context);
    }

    public static boolean getSendToBackground(Context context){
        return reader(context).getBoolean(SEND_TO_BACKGROUND_KEY, false);
    }

    public static void setWaitTillOffPhone(Context context, boolean enabled){
        editor(context).putBoolean(WAIT_TILL_OFF_PHONE_KEY, enabled);
        commit(context);
    }

    public static boolean getWaitTillOffPhone(Context context){
        return reader(context).getBoolean(WAIT_TILL_OFF_PHONE_KEY, true);
    }

    //Commits write to SharedPreferences
    private static void commit(Context context){
        editor(context).commit();
        _editor = null;
    }
}