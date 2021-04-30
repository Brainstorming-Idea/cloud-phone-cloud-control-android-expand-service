package com.muse.ocrdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.control.expand.service.aidl.TargetImg;
import com.cloud.control.expand.service.module.ocr.IOCRService;
import com.cloud.control.expand.service.module.ocr.InitModelListener;
import com.cloud.control.expand.service.module.ocr.OnResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.muse.ocrdemo.MainActivity.OPEN_GALLERY_REQUEST_CODE;

public class OcrDemoActivity extends AppCompatActivity {
    private static final String TAG = "OcrDemoActivity";

    private TextView picPathText;
    private Button openGallery;
    private ImageView targetImg;
    private Bitmap bitmap;
    private EditText etConfidence;
    private Button xunhuanBtn;
    public static final int OPEN_GALLERY_REQUEST_CODE = 0;
    private IOCRService iocrService;
    private Intent intent = new Intent();
    private String wordJson;
    private boolean isBind = false;
    private String currentPhotoPath;
    protected int cpuThreadNum = 4;
    protected String cpuPowerMode = "LITE_POWER_HIGH";
    protected String inputColorFormat = "BGR";
    protected float scoreThreshold = 0.1f;
    private TextView config;
    private TextView result;
    private TextView time;
    private Button start;
//    protected ProgressDialog pbLoadModel = null;
//    protected ProgressDialog pbRunModel = null;
    private boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_demo);
        picPathText = findViewById(R.id.pic_dir);
        openGallery = findViewById(R.id.open_gallery);
        targetImg = findViewById(R.id.iv_input_image);
        result = findViewById(R.id.tv_output_result);
        time = findViewById(R.id.tv_inference_time);
        etConfidence = findViewById(R.id.et_confidence);
        start = findViewById(R.id.start);
        xunhuanBtn = findViewById(R.id.xunhuan);
        result.setMovementMethod(ScrollingMovementMethod.getInstance());
        etConfidence.setText(scoreThreshold+"");
        /*绑定服务*/
        intent.setComponent(new ComponentName("com.cloud.control.expand.service", "com.cloud.control.expand.service.module.ocr.OcrService"));
        boolean bindSuccess = bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        if (!bindSuccess) {
            openGallery.setEnabled(false);
            etConfidence.setEnabled(false);
            Toast.makeText(OcrDemoActivity.this, "无法绑定服务", Toast.LENGTH_SHORT).show();
            return;
        }

        openGallery.setOnClickListener(v -> {
            if (requestAllPermissions()) {
                openGallery();
            }
        });
        setInputRule(4,etConfidence);
        start.setOnClickListener(v -> {
            scoreThreshold = Float.parseFloat(etConfidence.getText().toString());
            sendSettingParams();
        });
        xunhuanBtn.setOnClickListener(v -> {
            if ("停止".equals(xunhuanBtn.getText().toString())){
                xunhuanBtn.setText("循环识别");

            }else {
                xunhuanBtn.setText("停止");

                recogImg(bitmap);
            }
        });
        if (TextUtils.isEmpty(picPathText.getText())){
            start.setEnabled(false);
        }
    }

    /**
     * 服务绑定结果回调
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            openGallery.setEnabled(true);
            etConfidence.setEnabled(true);
            iocrService = IOCRService.Stub.asInterface(service);
            isBind = true;
            isInit = true;
            Toast.makeText(OcrDemoActivity.this, "绑定服务成功", Toast.LENGTH_SHORT).show();
            try {
                iocrService.initModel(initModelListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            openGallery.setEnabled(false);
            etConfidence.setEnabled(false);
            isBind = false;
            Toast.makeText(OcrDemoActivity.this, "绑定服务失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void openGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
    }

    /**
     * 模型初始化监听
     */
    public InitModelListener initModelListener = new InitModelListener.Stub() {
        @Override
        public void onLoadSuccess() throws RemoteException {
//            if (isInit) {
//                recognitionPic("images/5.jpg");
//            }
        }

        @Override
        public void onLoadFailed(String msg) throws RemoteException {
            Log.e(TAG, msg);
        }

    };

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
//                    pbRunModel = ProgressDialog.show(OcrDemoActivity.this, "", "正在识别...", false, false);
                    result.setText("正在识别图片...");
                }
            });
            Log.d(TAG, "传输图片");
            sendImgToRemote(image);
        } catch (IOException e) {
            Toast.makeText(OcrDemoActivity.this, "Load image failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//                    }
//                }).start();
    }

    /*识别结果*/
    private OnResultListener resultListener = new OnResultListener.Stub() {
        @Override
        public void onSuccess(final String recognitionRes, final float inferenceTime) throws RemoteException {
            Log.d(TAG, recognitionRes + "inferenceTime：" + inferenceTime);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    openGallery.setEnabled(true);
                    start.setEnabled(true);
//                    recogImg(bitmap);
//                    pbRunModel.dismiss();
//                    result.setText(recognitionRes);
                    Log.i(TAG, "识别结果："+recognitionRes);
                    try {
                        JSONArray jsonArray = new JSONArray(recognitionRes);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            sb.append(i + 1).append(": ").append(jsonObject.get("word")).append("\n");
                        }
                        result.setText(sb.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    openGallery.setEnabled(true);
                    start.setEnabled(true);
//                    pbRunModel.dismiss();
                    result.setText(msg);
                }
            });

        }
    };

    public void sendImgToRemote(Bitmap bitmap) {
//        if (!TextUtils.isEmpty(picPathText.getText())){
//            start.setEnabled(true);
//        }
        openGallery.setEnabled(false);
        start.setEnabled(false);
        xunhuanBtn.setText("停止");
        if (isBind) {
            TargetImg targetImg = new TargetImg();
            targetImg.setTarget(bitmap);
            result.setText("正在识别图片...");
            try {
                iocrService.inputImg(targetImg, resultListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "未绑定远程服务");
        }
    }

    public void recogImg(Bitmap bitmap) {
//        if (!TextUtils.isEmpty(picPathText.getText())){
//            start.setEnabled(true);
//        }
        openGallery.setEnabled(false);
        start.setEnabled(false);
        xunhuanBtn.setText("停止");
        if (isBind) {
            TargetImg targetImg = new TargetImg();
            targetImg.setTarget(bitmap);
            result.setText("正在识别图片...");
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
//            if (pbRunModel != null && !pbRunModel.isShowing()) {
//                //配置改变后调用远端接口设置一下
//                pbRunModel = ProgressDialog.show(OcrDemoActivity.this, "", "正在识别...", false, false);
//            }
            result.setText("正在识别图片...");
            openGallery.setEnabled(false);
            start.setEnabled(false);
            try {
                iocrService.advancedSetup(scoreThreshold, cpuThreadNum, cpuPowerMode);
            } catch (RemoteException e) {
                Log.e(TAG, "remote error:"+e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "未绑定远程服务");
            Toast.makeText(this,"未绑定远程服务",Toast.LENGTH_SHORT).show();
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
                        assert uri != null;
//                        picPathText.setText(getRealPathFromUri_AboveApi19(this, uri));
                        picPathText.setText(FileUtil.getFilePathByUri(this, uri));
                        Bitmap image = MediaStore.Images.Media.getBitmap(resolver, uri);
                        targetImg.setImageBitmap(image);
                        bitmap = image;
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, proj, null, null, null);
                        cursor.moveToFirst();
                        sendImgToRemote(image);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 禁止EditText输入特殊字符/空格/中文
     * 注意：这个方法会覆盖xml中设置的规则信息
     * @param maxLength 可输入的最大字符数
     * @param editTexts
     */
    public void setInputRule(int maxLength, @NonNull EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (" ".equals(source)) {
                        return "";
                    }
                    String speChat = "[\\u4E00-\\u9FA5\\u0020`~✘!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，\\-、_/\\---？]";
                    Pattern pattern = Pattern.compile(speChat);
                    Matcher matcher = pattern.matcher(source.toString());
                    if (matcher.find()) {
                        return "";
                    } else {
                        return null;
                    }
                }
            },new InputFilter.LengthFilter(maxLength)});
        }
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

}