package maderski.bluetoothautoplaymusic.controls.playercontrols.players

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import maderski.bluetoothautoplaymusic.controls.playercontrols.PlayerControls
import maderski.bluetoothautoplaymusic.sharedprefs.BAPMPreferences

internal class OtherMusicPlayer(context: Context) : PlayerControls(context) {

    @Synchronized
    override fun play() {
        Log.d(TAG, "Other Play Music")
        val packageName = BAPMPreferences.getPkgSelectedMusicPlayer(mContext)

        val downIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent)
        downIntent.setPackage(packageName)
        mContext.sendOrderedBroadcast(downIntent, null)

        val upIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY)
        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent)
        upIntent.setPackage(packageName)
        mContext.sendOrderedBroadcast(upIntent, null)
    }

    companion object {
        private const val TAG = "OtherMusicPlayer"
    }

}