package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodeActivity extends AppCompatActivity {

    EditText editText;
    Button genearate,scan;
    ImageView imageView;

    String[] appPermission = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
    int PERMISSION_CODE = 213;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        editText = findViewById(R.id.qrcode_value);
        genearate = findViewById(R.id.qrcode_generate);
        scan = findViewById(R.id.qrcode_scanner);
        imageView = findViewById(R.id.qrcode_image);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermission()) {
                    scanRedirect();
                }
            }
        });

        genearate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().equals("")){
                    editText.setError("QR Code Value Required");
                }
                else{
                    int MARGIN_AUTOMATIC = -1;
                    int MARGIN_NONE = 0;
                    int marginSize = MARGIN_NONE;

                    Map<EncodeHintType, Object> hints = null;
                    if (marginSize != MARGIN_AUTOMATIC) {
                        hints = new EnumMap<>(EncodeHintType.class);
                        // We want to generate with a custom margin size
                        hints.put(EncodeHintType.MARGIN, marginSize);
                    }

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE, 300, 300, hints);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageView.setImageBitmap(bitmap);
                        editText.setText("");

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void scanRedirect() {
        new CommonMethodClass(QRCodeActivity.this, QrCodeScannerActivity.class);
    }

    private boolean checkAndRequestPermission() {
        List<String> listPermission = new ArrayList<>();
        for(String perm : appPermission){
            if(ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED){
                listPermission.add(perm);
            }
        }

        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),PERMISSION_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            HashMap<String,Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResult.put(permissions[i],grantResults[i]);
                    deniedCount++;
                }
            }

            if(deniedCount==0){
                scanRedirect();
            }
            else{
                for(Map.Entry<String,Integer> entry : permissionResult.entrySet()){
                    String perName = entry.getKey();
                    int permResult = entry.getValue();

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,perName)){
                        showDialogPermission(
                                "",
                                "This App Needs Read Storage And Camera Permissions To Work Without Any Problems",
                                "Yes, Grant Permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkAndRequestPermission();
                                    }
                                },
                                "No, Exit App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finishAffinity();
                                    }
                                }
                        );
                    }
                    else{
                        showDialogPermission(
                                "",
                                "You Have Denied Some Permission. Allow All Permissions",
                                "Go To Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getPackageName(),null));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                                "No, Exit App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finishAffinity();
                                    }
                                }
                        );
                        break;
                    }

                }
            }
        }
    }

    public AlertDialog showDialogPermission(
            String title,
            String msg,
            String positiveLable,
            DialogInterface.OnClickListener positiveClick,
            String negativeLable,
            DialogInterface.OnClickListener negativeClick){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLable,positiveClick);
        builder.setNegativeButton(negativeLable,negativeClick);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

}