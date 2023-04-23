package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationTextActivity extends AppCompatActivity {

    TextView locationText;
    String[] appPermission = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    int CONTACT_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_text);
        locationText = findViewById(R.id.location_textview);
        if (checkAndRequestPermission()) {
            fetchLocation();
        }
    }

    public boolean checkAndRequestPermission() {
        List<String> listPermission = new ArrayList<>();
        for (String perm : appPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(perm);
            }
        }
        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), CONTACT_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CONTACT_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                fetchLocation();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        /*showDialogPermission("", "This App needs Read External Storage And Location permissions to work whithout and problems.",*/
                        showDialogPermission("", "This App needs Access Location permissions to work whithout and problems.",
                                "Yes, Grant permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        checkAndRequestPermission();
                                    }
                                },
                                "No, Exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finishAffinity();
                                    }
                                }, false);
                    } else {
                        showDialogPermission("", "You have denied some permissions. Allow all permissions at [Setting] > [Permissions]",
                                "Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, "No, Exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    public AlertDialog showDialogPermission(String title, String msg, String positiveLable, DialogInterface.OnClickListener positiveOnClickListener, String negativeLable, DialogInterface.OnClickListener negativeOnClickListener, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLable, positiveOnClickListener);
        builder.setNegativeButton(negativeLable, negativeOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    private void fetchLocation() {
        GPSTracker gpsTracker = new GPSTracker(LocationTextActivity.this);
        if(gpsTracker.canGetLocation()){
            double lat = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Log.d("RESPONSE_LOCATION",gpsTracker.getLatitude()+"\n"+gpsTracker.getLongitude());

            Geocoder geocoder = new Geocoder(LocationTextActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat,longitude,1);
                if(addresses==null || addresses.size()<=0){
                    Log.d("RESPONSE","Null");
                }
                else{
                    String sAddress = addresses.get(0).getAddressLine(0);
                    String sCity = addresses.get(0).getLocality();
                    String sSubCity = addresses.get(0).getSubLocality();
                    String sState = addresses.get(0).getAdminArea();
                    String sCountryCode = addresses.get(0).getCountryCode();
                    String sCountry = addresses.get(0).getCountryName();
                    String sPostCode = addresses.get(0).getPostalCode();
                    String sKnownName = addresses.get(0).getFeatureName();

                    locationText.setText(
                            "Latitude : "+lat+
                                    "\nLongitude : "+longitude+
                                    "\n\nAddress : "+sAddress+
                                    "\n\nCity : "+sCity+
                                    "\n\nSub City : "+sSubCity+
                                    "\n\nState : "+sState+
                                    "\n\nCountry : "+sCountry+" ("+sCountryCode+")"+
                                    "\n\nPostCode : "+sPostCode+
                                    "\n\nKnown Name : "+sKnownName);

                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("RESPONSE",e.getMessage());
            }

        }
    }
}