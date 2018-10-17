package com.abrahamyan.pl.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.abrahamyan.pl.BuildConfig;
import com.abrahamyan.pl.util.Constant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SEVAK on 02.08.2017.
 */

public class CameraActivity extends Activity {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = CameraActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean useCamera = true;
    String pictureFilePath;
    File file;
    Uri fileUri;

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startCameraPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.RequestCode.CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                useCamera = false;
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.RequestCode.CAMERA:
                    Intent intent = new Intent();
                    File imgFile = new  File(pictureFilePath);
                    if(imgFile.exists())            {
                        intent.putExtra(Constant.Extra.EXTRA_PHOTO_URI, Uri.fromFile(imgFile));
                    }
                    setResult(RESULT_OK, intent);
                    finish();
            }
        } else {
            finish();
        }
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void startCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        Constant.RequestCode.CAMERA);
            }
        } else if (useCamera) {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            pictureFile = getPictureFile();
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".my.package.name.provider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, Constant.RequestCode.CAMERA);
            }
        }
    }

    private File getPictureFile()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ZOFTINO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        } catch (IOException e) {
            Toast.makeText(this,
                    "Photo file can't be created, please try again",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        pictureFilePath = image.getAbsolutePath();
        return image;
    }
}