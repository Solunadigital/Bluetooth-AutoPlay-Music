package maderski.bluetoothautoplaymusic.controls.playercontrols.players

import android.content.Context
import android.content.Intent
import maderski.bluetoothautoplaymusic.PackageTools
import maderski.bluetoothautoplaymusic.controls.playercontrols.PlayerControls

internal class GooglePlayMusic(context: Context) : PlayerControls(context) {

    override fun play() {
        val intent = Intent("com.android.music.musicservicecommand")
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        intent.putExtra("command", "play")
        intent.setPackage(PackageTools.PackageName.GOOGLEPLAYMUSIC)
        mContext.sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "GooglePlayMusic"
    }
}