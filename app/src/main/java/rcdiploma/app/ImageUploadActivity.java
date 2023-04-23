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
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rcdiploma.app.RetrofitClass.ImageUploadData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploadActivity extends AppCompatActivity {

    EditText name;
    Button selectIv, uploadIv;
    ImageView imageView;

    int IMAGE_CODE = 123;
    Uri filePath;
    String sSelectedPath;

    int STORAGE_CODE = 1000;
    ProgressDialog pd;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        requestStoragePermission();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        name = findViewById(R.id.image_upload_name);
        selectIv = findViewById(R.id.image_upload_select);
        uploadIv = findViewById(R.id.image_upload_submit);
        imageView = findViewById(R.id.image_upload_iv);

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_CODE);
            }
        });

        uploadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                } else {
                    if (sSelectedPath == null || sSelectedPath == "") {
                        new CommonMethodClass(ImageUploadActivity.this, "Please Select Image");
                    } else {
                        pd = new ProgressDialog(ImageUploadActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();

                        doImageUpload();

                        /*try {
                            new MultipartUploadRequest(ImageUploadActivity.this, ConstantUrl.URL + "addCategory.php")
                                    .addParameter("name", name.getText().toString())
                                    .addFileToUpload(sSelectedPath, "file")
                                    .setMaxRetries(2)
                                    .startUpload();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new CommonMethodClass(ImageUploadActivity.this, "Category Added Successfully");
                                    new CommonMethodClass(ImageUploadActivity.this, CategoryActivity.class);
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

    private void doImageUpload() {
        RequestBody nameBody = RequestBody.create(MultipartBody.FORM,name.getText().toString());

        File file = new File(sSelectedPath);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file",file.getName(),RequestBody.create(MediaType.parse("image/*"),file));

        Call<ImageUploadData> call = apiInterface.imageUploadData(nameBody,imagePart);
        call.enqueue(new Callback<ImageUploadData>() {
            @Override
            public void onResponse(Call<ImageUploadData> call, Response<ImageUploadData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        new CommonMethodClass(ImageUploadActivity.this,response.body().message);
                        new CommonMethodClass(ImageUploadActivity.this, CategoryActivity.class);
                    }
                    else{
                        new CommonMethodClass(ImageUploadActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(ImageUploadActivity.this,ConstantUrl.SERVER_ERROR+" "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ImageUploadData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(ImageUploadActivity.this,t.getMessage());
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
                new CommonMethodClass(ImageUploadActivity.this, "Permission Granted");
            } else {
                new CommonMethodClass(ImageUploadActivity.this, "You Just Denied Permission");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            imageView.setVisibility(View.VISIBLE);
            uploadIv.setVisibility(View.VISIBLE);
            filePath = data.getData();
            imageView.setImageURI(filePath);
            sSelectedPath = getImage(filePath);
            Log.d("RESPONSE_URI", filePath.toString());
            Log.d("RESPONSE_PATH", sSelectedPath);
        }
    }

    private String getImage(Uri uri) {
        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, s_array, null, null, null);
            int id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                do {
                    path = cursor.getString(id);
                }
                while (cursor.moveToNext());
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

}