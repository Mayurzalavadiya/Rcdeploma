package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rcdiploma.app.RetrofitClass.DocumentUploadData;
import rcdiploma.app.RetrofitClass.DocumentUploadData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentUploadActivity extends AppCompatActivity {

    EditText name;
    Button selectIv, uploadIv;
    TextView textPath;

    int PDF_CODE = 123;
    String sSelectedPath;

    int STORAGE_CODE = 1000;
    ProgressDialog pd;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        requestStoragePermission();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        name = findViewById(R.id.document_upload_name);
        selectIv = findViewById(R.id.document_upload_select);
        uploadIv = findViewById(R.id.document_upload_submit);
        textPath = findViewById(R.id.document_upload_path);

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_CODE);
            }
        });

        uploadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                } else {
                    if (sSelectedPath == null || sSelectedPath == "") {
                        new CommonMethodClass(DocumentUploadActivity.this, "Please Select Pdf");
                    } else {
                        pd = new ProgressDialog(DocumentUploadActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        
                        doFileUpload();
                        
                        /*try {
                            new MultipartUploadRequest(DocumentUploadActivity.this, ConstantUrl.URL + "addDocument.php")
                                    .addParameter("name", name.getText().toString())
                                    .addFileToUpload(sSelectedPath, "file")
                                    .setMaxRetries(2)
                                    .startUpload();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new CommonMethodClass(DocumentUploadActivity.this, "Document Added Successfully");
                                    new CommonMethodClass(DocumentUploadActivity.this, DocumentUploadActivity.class);
                                }
                            }, 5000);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }*/

                    }
                }
            }
        });

    }

    private void doFileUpload() {
        RequestBody nameBody = RequestBody.create(MultipartBody.FORM,name.getText().toString());

        File file = new File(sSelectedPath);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file",file.getName(),RequestBody.create(MediaType.parse("pdf/*"),file));

        Call<DocumentUploadData> call = apiInterface.documentUploadData(nameBody,imagePart);
        call.enqueue(new Callback<DocumentUploadData>() {
            @Override
            public void onResponse(Call<DocumentUploadData> call, Response<DocumentUploadData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        new CommonMethodClass(DocumentUploadActivity.this,response.body().message);
                        new CommonMethodClass(DocumentUploadActivity.this, DocumentUploadActivity.class);
                    }
                    else{
                        new CommonMethodClass(DocumentUploadActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(DocumentUploadActivity.this,ConstantUrl.SERVER_ERROR+" "+response.code());
                }
            }

            @Override
            public void onFailure(Call<DocumentUploadData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(DocumentUploadActivity.this,t.getMessage());
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new CommonMethodClass(DocumentUploadActivity.this, "Permission Granted");
            } else {
                new CommonMethodClass(DocumentUploadActivity.this, "You Just Denied Permission");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_CODE && resultCode == RESULT_OK && data != null) {
            textPath.setVisibility(View.VISIBLE);
            uploadIv.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sSelectedPath = FilePath.getPath(DocumentUploadActivity.this,data.getData());
                textPath.setText(sSelectedPath);
            }
            Log.d("RESPONSE_PATH", sSelectedPath);
        }
    }
}