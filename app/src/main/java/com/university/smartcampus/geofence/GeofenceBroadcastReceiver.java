package com.university.smartcampus.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.university.smartcampus.utils.AudioUtils;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("GEOFENCE_ERROR", "Error: " + geofencingEvent.getErrorCode());
            return;
        }

        int transition = geofencingEvent.getGeofenceTransition();
        List<Geofence> triggeredFences = geofencingEvent.getTriggeringGeofences();

        for (Geofence fence : triggeredFences) {
            String fenceId = fence.getRequestId();

            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                if (fenceId.equals("CLASSROOM")) {
                    AudioUtils.setSilentMode(context, true); // Mute phone
                } else if (fenceId.equals("DORMITORY")) {
                    AudioUtils.playWelcomeSound(context); // Play welcome sound
                }
            } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                if (fenceId.equals("CLASSROOM")) {
                    AudioUtils.setSilentMode(context, false); // Unmute phone
                }
            }
        }
    }
}