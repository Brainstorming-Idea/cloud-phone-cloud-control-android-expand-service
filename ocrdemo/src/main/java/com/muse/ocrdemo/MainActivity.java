package com.muse.ocrdemo;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloud.control.expand.service.aidl.TargetImg;
import com.cloud.control.expand.service.module.ocr.IOCRService;
import com.cloud.control.expand.service.module.ocr.OnResultListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyou
 * @desc:
 * @date :2021/2/4
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private IOCRService iocrService;
    private Intent intent = new Intent();
    private String wordJson;
    private boolean isBind = false;
    private String currentPhotoPath;
    public static final int OPEN_GALLERY_REQUEST_CODE = 0;
    public static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bindBtn = findViewById(R.id.button);
        Button sendBtn = findViewById(R.id.button2);
        Button picBtn = findViewById(R.id.button3);
        bindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setComponent(new ComponentName("com.cloud.control.expand.service","com.cloud.control.expand.service.module.ocr.OcrService"));
                boolean bindSuccess = bindService(intent, serviceConnection , Service.BIND_AUTO_CREATE);
                Toast.makeText(MainActivity.this,"绑定服务:" + bindSuccess,Toast.LENGTH_SHORT).show();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestAllPermissions()){
                    takePhoto();
                }
            }
        });
        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imagePath = "images/5.jpg";
                            Bitmap image = null;
                            // Read test image file from custom path if the first character of mode path is '/', otherwise read test
                            // image file from assets
                            if (!imagePath.substring(0, 1).equals("/")) {
                                InputStream imageStream = getAssets().open(imagePath);
                                image = BitmapFactory.decodeStream(imageStream);
                            } else {
                                if (!new File(imagePath).exists()) {
                                    return;
                                }
                                image = BitmapFactory.decodeFile(imagePath);
                            }
                            sendImgToRemote(image);
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Load image failed!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iocrService = IOCRService.Stub.asInterface(service);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    private OnResultListener resultListener = new OnResultListener.Stub() {
        @Override
        public void onSuccess(String recognitionRes) throws RemoteException {
            Log.d(TAG,recognitionRes);
        }

        @Override
        public void onFailed(String msg) throws RemoteException {
            Log.e(TAG, msg);
        }
    };

    public void sendImgToRemote(Bitmap bitmap){
        if (isBind){
            TargetImg targetImg = new TargetImg();

            targetImg.setTarget(bitmap);
            try {
                iocrService.inputImg(targetImg, "xxx", 1.0f, 0, resultListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean requestAllPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    0);
            return false;
        }
        return true;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("MainActitity", ex.getMessage(), ex);
                Toast.makeText(MainActivity.this,
                        "Create Camera temp file failed: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i(TAG, "FILEPATH " + getExternalFilesDir("Pictures").getAbsolutePath());
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.muse.ocrdemo.fileprovider",
                        photoFile);
                currentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
                Log.i(TAG, "startActivityForResult finished");
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".bmp",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_GALLERY_REQUEST_CODE:
                    if (data == null) {
                        break;
                    }
                    try {
                        ContentResolver resolver = getContentResolver();
                        Uri uri = data.getData();
                        Bitmap image = MediaStore.Images.Media.getBitmap(resolver, uri);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, proj, null, null, null);
                        cursor.moveToFirst();
//                        onImageChanged(image);
                        sendImgToRemote(image);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
                case TAKE_PHOTO_REQUEST_CODE:
                    if (currentPhotoPath != null) {
                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(currentPhotoPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        Log.i(TAG, "rotation " + orientation);
                        Bitmap image = BitmapFactory.decodeFile(currentPhotoPath);
                        image = Utils.rotateBitmap(image, orientation);
//                        onImageChanged(image);
                        sendImgToRemote(image);
                    } else {
                        Log.e(TAG, "currentPhotoPath is null");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
