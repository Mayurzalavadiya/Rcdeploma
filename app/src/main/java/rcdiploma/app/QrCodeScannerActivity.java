package rcdiploma.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QrCodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    ZBarScannerView zBarScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        zBarScannerView = new ZBarScannerView(this);
        setContentView(zBarScannerView);
        getSupportActionBar().hide();
        zBarScannerView.setResultHandler(this);
        zBarScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.beep);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            vibrator.vibrate(500);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(QrCodeScannerActivity.this);
        builder.setTitle("QR Code Scanner Result");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Your QR Code Scanner Result : "+result.getContents());
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        builder.show();
    }
}