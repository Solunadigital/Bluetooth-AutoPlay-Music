package maderski.bluetoothautoplaymusic.helpers

import android.content.Intent
import android.util.Log
import maderski.bluetoothautoplaymusic.maps.MapApps.MAPS
import maderski.bluetoothautoplaymusic.maps.MapApps.WAZE
import maderski.bluetoothautoplaymusic.helpers.enums.MediaPlayers
import maderski.bluetoothautoplaymusic.wrappers.PackageManagerWrapper
import maderski.bluetoothautoplaymusic.wrappers.SystemServicesWrapper

/**
 * Created by Jason on 7/28/16.
 */
class PackageHelper(
        private val systemServicesWrapper: SystemServicesWrapper,
        private val packageManagerWrapper: PackageManagerWrapper
) {
    // Launches App that is associated with that package that was put into method
    fun getLaunchIntent(packageName: String): Intent? {
        Log.d("Package intent: ", "$packageName started")
        return packageManagerWrapper.getLaunchIntentForPackage(packageName)

    }

    //Returns true if Package is on phone
    fun isPackageOnPhone(packageName: String): Boolean =
            packageManagerWrapper.getAllInstalledPackages()
                    .any { packageInfo ->
                        packageInfo.packageName == packageName
                    }

    // Set of Mediaplayers that is installed on the phone
    fun installedMediaPlayersSet(): Set<String> {
        val actionMediaButtonReceivers = packageManagerWrapper.getActionMediaButtonReceivers()
        val installedMediaPlayers = actionMediaButtonReceivers.filter { resolveInfo ->
            val resolveInfoString = resolveInfo.toString()
            resolveInfoString.contains(".playback")
                    || resolveInfoString.contains("music")
                    || resolveInfoString.contains("Music")
                    || resolveInfoString.contains("audioplayer")
        }.map { musicPlayerResolveInfo ->
            val musicPlayerRIString = musicPlayerResolveInfo.toString()
            val resolveInfoSplit = musicPlayerRIString.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            resolveInfoSplit[1].substring(0, resolveInfoSplit[1].indexOf("/"))
        }.toMutableSet()

        val mediaPlayerPackagesToAdd = MediaPlayers.values().map { it.packageName }
        val mediaPlayersInstalled = mediaPlayerPackagesToAdd.filter { packageName ->
            isPackageOnPhone(packageName)
        }
        installedMediaPlayers.addAll(mediaPlayersInstalled)

        return installedMediaPlayers
    }

    // Returns Map App Name, intentionally only works with Google maps and Waze
    fun getMapAppName(packageName: String): String {
        return try {
            val applicationLabel = packageManagerWrapper.getApplicationLabel(packageName)
            if (applicationLabel.equals(MAPS.applicationLabel, ignoreCase = true)) {
                WAZE.uiDisplayName
            } else {
                MAPS.uiDisplayName
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Not Found"
        }
    }

    fun getActivityManager() = systemServicesWrapper.activityManager

    companion object {
        private const val TAG = "PackageHelper"
    }
}
