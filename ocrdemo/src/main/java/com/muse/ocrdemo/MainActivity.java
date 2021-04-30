package com.muse.ocrdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.control.expand.service.aidl.TargetImg;
import com.cloud.control.expand.service.module.ocr.IOCRService;
import com.cloud.control.expand.service.module.ocr.InitModelListener;
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
    // Model settings of object detection
    protected String modelPath = "";
    protected String labelPath = "";
    protected String imagePath = "";
    protected int cpuThreadNum = 4;
    protected String cpuPowerMode = "LITE_POWER_HIGH";
    protected String inputColorFormat = "BGR";
    protected long[] inputShape = new long[]{};
    protected float[] inputMean = new float[]{};
    protected float[] inputStd = new float[]{};
    protected float scoreThreshold = 0.1f;

    private TextView config;
    private ImageView targetImg;
    private TextView result;
    private TextView time;
    protected ProgressDialog pbLoadModel = null;
    protected ProgressDialog pbRunModel = null;
    private boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        pbRunModel = ProgressDialog.show(MainActivity.this,"","正在识别...",false, false);
        /*绑定服务*/
        intent.setComponent(new ComponentName("com.cloud.control.expand.service", "com.cloud.control.expand.service.module.ocr.OcrService"));
        boolean bindSuccess = bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        if (!bindSuccess) {
            Toast.makeText(MainActivity.this, "无法绑定服务", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean settingsChanged = false;
//        String model_path = sharedPreferences.getString(getString(R.string.MODEL_PATH_KEY),
//                getString(R.string.MODEL_PATH_DEFAULT));
//        String label_path = sharedPreferences.getString(getString(R.string.LABEL_PATH_KEY),
//                getString(R.string.LABEL_PATH_DEFAULT));
//        String image_path = sharedPreferences.getString(getString(R.string.IMAGE_PATH_KEY),
//                getString(R.string.IMAGE_PATH_DEFAULT));
//        settingsChanged |= !model_path.equalsIgnoreCase(modelPath);
//        settingsChanged |= !label_path.equalsIgnoreCase(labelPath);
//        settingsChanged |= !image_path.equalsIgnoreCase(imagePath);
        int cpu_thread_num = Integer.parseInt(sharedPreferences.getString(getString(R.string.CPU_THREAD_NUM_KEY),
                getString(R.string.CPU_THREAD_NUM_DEFAULT)));
        settingsChanged |= cpu_thread_num != cpuThreadNum;
        String cpu_power_mode =
                sharedPreferences.getString(getString(R.string.CPU_POWER_MODE_KEY),
                        getString(R.string.CPU_POWER_MODE_DEFAULT));
        settingsChanged |= !cpu_power_mode.equalsIgnoreCase(cpuPowerMode);
        String input_color_format =
                sharedPreferences.getString(getString(R.string.INPUT_COLOR_FORMAT_KEY),
                        getString(R.string.INPUT_COLOR_FORMAT_DEFAULT));
        settingsChanged |= !input_color_format.equalsIgnoreCase(inputColorFormat);
//        long[] input_shape =
//                Utils.parseLongsFromString(sharedPreferences.getString(getString(R.string.INPUT_SHAPE_KEY),
//                        getString(R.string.INPUT_SHAPE_DEFAULT)), ",");
//        float[] input_mean =
//                Utils.parseFloatsFromString(sharedPreferences.getString(getString(R.string.INPUT_MEAN_KEY),
//                        getString(R.string.INPUT_MEAN_DEFAULT)), ",");
//        float[] input_std =
//                Utils.parseFloatsFromString(sharedPreferences.getString(getString(R.string.INPUT_STD_KEY)
//                        , getString(R.string.INPUT_STD_DEFAULT)), ",");
//        settingsChanged |= input_shape.length != inputShape.length;
//        settingsChanged |= input_mean.length != inputMean.length;
//        settingsChanged |= input_std.length != inputStd.length;
//        if (!settingsChanged) {
//            for (int i = 0; i < input_shape.length; i++) {
//                settingsChanged |= input_shape[i] != inputShape[i];
//            }
//            for (int i = 0; i < input_mean.length; i++) {
//                settingsChanged |= input_mean[i] != inputMean[i];
//            }
//            for (int i = 0; i < input_std.length; i++) {
//                settingsChanged |= input_std[i] != inputStd[i];
//            }
//        }
        float score_threshold =
                Float.parseFloat(sharedPreferences.getString(getString(R.string.SCORE_THRESHOLD_KEY),
                        getString(R.string.SCORE_THRESHOLD_DEFAULT)));
        settingsChanged |= scoreThreshold != score_threshold;
        if (settingsChanged) {
//            modelPath = model_path;
//            labelPath = label_path;
//            imagePath = image_path;
            cpuThreadNum = cpu_thread_num;
            cpuPowerMode = cpu_power_mode;
            inputColorFormat = input_color_format;
//            inputShape = input_shape;
//            inputMean = input_mean;
//            inputStd = input_std;
            scoreThreshold = score_threshold;
            // Update UI
            config.setText("CPU线程数：" + cpuThreadNum + "\n" + "CPU性能：" + cpuPowerMode + "\n" + "颜色格式：" + inputColorFormat+"\n"+"置信度："+scoreThreshold);
//            tvInputSetting.setText("Model: " + modelPath.substring(modelPath.lastIndexOf("/") + 1) + "\n" + "CPU" +
//                    " Thread Num: " + Integer.toString(cpuThreadNum) + "\n" + "CPU Power Mode: " + cpuPowerMode);
//            tvInputSetting.scrollTo(0, 0);
            // Reload model if configure has been changed
        }
        sendSettingParams();
    }

    private void recognitionPic(String imagePath) {
//        new Thread(new Runnable() {
//                    @Override
//                    public void run() {
        try {
//                            imagePath = "images/5.jpg";
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
            final Bitmap finalImage = image;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    targetImg.setImageBitmap(finalImage);
                    pbRunModel = ProgressDialog.show(MainActivity.this, "", "正在识别...", false, false);
                }
            });
            Log.d(TAG, "传输图片");
            sendImgToRemote(image);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Load image failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//                    }
//                }).start();
    }

    public void initView() {
        config = findViewById(R.id.tv_config);
        targetImg = findViewById(R.id.target_img);
        result = findViewById(R.id.tv_result);
        time = findViewById(R.id.tv_time);
        result.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.open_gallery:
                if (requestAllPermissions()) {
//                    openGallery();
                    openPictures();
                }
                break;
//            case R.id.take_photo:
//                if (requestAllPermissions()) {
//                    takePhoto();
//                }
//                break;
            case R.id.settings:
                if (requestAllPermissions()) {
                    // Make sure we have SDCard r&w permissions to load model from SDCard
                    onSettingsClicked();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 服务绑定结果回调
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iocrService = IOCRService.Stub.asInterface(service);
            isBind = true;
            isInit = true;
            Toast.makeText(MainActivity.this, "绑定服务成功", Toast.LENGTH_SHORT).show();
            try {
                iocrService.initModel(initModelListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            Toast.makeText(MainActivity.this, "绑定服务失败", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 模型初始化监听
     */
    public InitModelListener initModelListener = new InitModelListener.Stub() {
        @Override
        public void onLoadSuccess() throws RemoteException {
            if (isInit) {
                recognitionPic("images/5.jpg");
            }
        }

        @Override
        public void onLoadFailed(String msg) throws RemoteException {
            Log.e(TAG, msg);
        }

    };

    /*识别结果*/
    private OnResultListener resultListener = new OnResultListener.Stub() {
        @Override
        public void onSuccess(final String recognitionRes, final float inferenceTime) throws RemoteException {
            Log.d(TAG, recognitionRes + "inferenceTime：" + inferenceTime);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbRunModel.dismiss();
                    result.setText(recognitionRes);
                    time.setText("识别耗时：" + inferenceTime + "ms");
                }
            });
        }

        @Override
        public void onFailed(final String msg) throws RemoteException {
            Log.e(TAG, msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbRunModel.dismiss();
                    result.setText(msg);
                }
            });

        }
    };

    public void sendImgToRemote(Bitmap bitmap) {
        if (isBind) {
            TargetImg targetImg = new TargetImg();
            targetImg.setTarget(bitmap);
            try {
                iocrService.inputImg(targetImg, resultListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "未绑定远程服务");
        }
    }

    /**
     * 设置高级参数
     */
    private void sendSettingParams() {
        if (isBind) {
            isInit = false;
            if (pbRunModel != null && !pbRunModel.isShowing()) {
                //配置改变后调用远端接口设置一下
                pbRunModel = ProgressDialog.show(MainActivity.this, "", "正在识别...", false, false);
            }
            try {
                iocrService.advancedSetup(scoreThreshold, cpuThreadNum, cpuPowerMode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "未绑定远程服务");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
    }

    private void openPictures() {
        Intent picIntent = new Intent(this, PicturesActivity.class);
        startActivityForResult(picIntent, OPEN_GALLERY_REQUEST_CODE);
    }

    public void onSettingsClicked() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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
//                    try {
//                        ContentResolver resolver = getContentResolver();
//                        Uri uri = data.getData();
//                        Bitmap image = MediaStore.Images.Media.getBitmap(resolver, uri);
//                        String[] proj = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = managedQuery(uri, proj, null, null, null);
//                        cursor.moveToFirst();
//                        onImageChanged(image);
                        String path = data.getDataString();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        targetImg.setImageBitmap(bitmap);
                        pbRunModel = ProgressDialog.show(MainActivity.this, "", "正在识别...", false, false);
                        sendImgToRemote(bitmap);
//                    } catch (IOException e) {
//                        Log.e(TAG, e.toString());
//                    }
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

    @Override
    protected void onStop() {
        super.onStop();
        if (pbRunModel != null && pbRunModel.isShowing()) {
            pbRunModel.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
