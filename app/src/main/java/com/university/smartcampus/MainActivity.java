package com.university.smartcampus;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.university.smartcampus.geofence.GeofenceManager;

public class MainActivity extends AppCompatActivity {
    private GeofenceManager geofenceManager;
    private TextView tvStatus;
    private static final int LOCATION_PERMISSION_CODE = 1001;
    private static final int BACKGROUND_PERMISSION_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        geofenceManager = new GeofenceManager(this);
        checkNotificationPolicyAccess();
        requestLocationPermissions();
    }

    public void onSetClassroom(View v) {
        // Classroom coordinates (replace with your actual coordinates)
        geofenceManager.addGeofence("CLASSROOM", 34.0522, -118.2437, 50);
        tvStatus.setText("Status: Classroom geofence active");
        Toast.makeText(this, "Classroom Geofence Active! Phone will mute when you enter.", Toast.LENGTH_SHORT).show();
    }

    public void onSetDorm(View v) {
        // Dormitory coordinates from your Google Maps link
        geofenceManager.addGeofence("DORMITORY", 7.046544, 38.500574, 20);
        tvStatus.setText("Status: Dormitory geofence active");
        Toast.makeText(this, "Dormitory Geofence Active! Welcome sound will play when you're within 5 meters.", Toast.LENGTH_SHORT).show();
    }

    // --- PERMISSION HANDLING ---
    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        } else {
            requestBackgroundLocationPermission();
        }
    }

    private void requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        BACKGROUND_PERMISSION_CODE);
            }
        }
    }

    private void checkNotificationPolicyAccess() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!nm.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
                Toast.makeText(this, "Please grant DND access for Silent Mode", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestBackgroundLocationPermission();
            }
        }
    }
}