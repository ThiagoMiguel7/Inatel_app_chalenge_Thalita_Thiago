package com.example.inatel_app_chalenge;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker currentLocationMaker;
    private LatLng currentLocationLatLong;
    private List<Installer> installers = new ArrayList<>();
    private Circle circle;
    Button button;
    private SeekBar seekBar;
    private double radius = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double km = Math.pow(10, progress) * 5;
                radius = km * 1000;
                if(circle != null) {
                    circle.setRadius(radius);
                    setPinsVisibility();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Plan plan = (Plan) getIntent().getSerializableExtra("plan");
        getInstallers(plan.id);

        startGettingLocations();
    }

    private void setPinsVisibility() {
        for(Installer i: installers) {
            float[] distance = new float[10];
            Location.distanceBetween(i.marker.getPosition().latitude,
                    i.marker.getPosition().longitude,
                    circle.getCenter().latitude,
                    circle.getCenter().longitude,
                    distance);
            i.marker.setVisible(distance[0] <= circle.getRadius());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(marker -> {
            Installer installer = null;
            for(Installer i: installers) {
                if (i.name.equals(marker.getTitle())) {
                    installer = i;
                    break;
                }
            }
            if(installer != null) {
                Intent intent = new Intent(this, InstallersActivity.class);
                intent.putExtra("installer", installer);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (currentLocationMaker != null) {
            currentLocationMaker.remove();
        }

        //Add marker
        currentLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocationLatLong);
        markerOptions.title("Localização atual");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMaker = mMap.addMarker(markerOptions);

        //Move to new location
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());

        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(radius)
                .strokeColor(Color.argb(0.2f, 1f, 0f, 0f))
                .fillColor(Color.argb(0.2f, 0f, 0f, 1f)));

        setPinsVisibility();

        Toast.makeText(this, "Localização atualizada", Toast.LENGTH_SHORT).show();
    }


    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desativado!");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    @SuppressLint("MissingPermission")
    private void startGettingLocations() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();

        } else {
            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }

        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            }   else if (isNetwork) {

                // from Network Provider
                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
        } else {
            Toast.makeText(this, "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    private void getInstallers(int planId) {
        Call<List<Installer>> call = RetrofitClient.getInstance().getMyApi().getInstallers(planId);
        call.enqueue(new Callback<List<Installer>>() {
            @Override
            public void onResponse(Call<List<Installer>> call, Response<List<Installer>> response) {
                installers = response.body();
                for(Installer i: installers) {
                    i.marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(i.latitude, i.longitude))
                            .visible(false)
                            .title(i.name));
                }
            }

            @Override
            public void onFailure(Call<List<Installer>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });
    }
}
