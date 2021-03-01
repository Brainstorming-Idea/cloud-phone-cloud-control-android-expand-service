package com.cloud.control.expand.service.module.ocr;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.aidl.TargetImg;
import com.cloud.control.expand.service.entity.ExpandService;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Subscriber;
import rx.functions.Action0;


/**
 * @author wangyou
 * @desc:
 * @date :2021/2/3
 */
public class OcrService extends Service {
    private static final String TAG = "OcrService";

    // Model settings of object detection
//    private boolean updateParams = false;
//    protected String modelPath = "";
//    protected String labelPath = "";
//    protected String imagePath = "";
//    protected int cpuThreadNum = 1;
//    protected String cpuPowerMode = "";
//    protected String inputColorFormat = "";
//    protected long[] inputShape = new long[]{};
//    protected float[] inputMean = new float[]{};
//    protected float[] inputStd = new float[]{};
//    protected float scoreThreshold = 0.1f;
//    private String currentPhotoPath;

    protected Predictor predictor = new Predictor();
    protected OcrParams ocrParams = null;
    protected TargetImg targetImg = null;
    protected List<Disposable> mDisposables = new ArrayList<>();
    private OnResultListener onResultListener = null;
    /**扩展服务是否到期*/
    private boolean isDeadline = false;

    public OcrService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //1. 加载模型
        loadModel(getDefOcrParams());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IOCRService.Stub binder = new IOCRService.Stub() {
        @Override
        public void inputImg(TargetImg targetImg, String imgFlag, float confidence, int moduleId, OnResultListener onResultListener) throws RemoteException {
            setOnResultListener(onResultListener);
            setImage(targetImg);

            //检查扩展服务到期时间
            checkDeadline();
//            onResultListener.onSuccess("进程间通信成功");
        }
    };

    public void setImage(TargetImg targetImg){
        this.targetImg = targetImg;
    }

    private void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    /**
     * 检查扩展服务是否到期
     */
    public void checkDeadline(){
        RetrofitServiceManager.getExtendServiceRecord()
                .subscribe(new Subscriber<ExpandServiceRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getExtendServiceRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getExtendServiceRecord onError");
                    }

                    @Override
                    public void onNext(ExpandServiceRecordEntity recordEntity) {
                        KLog.d("getExtendServiceRecord onNext " + recordEntity.toString());
                        if (recordEntity.getData() != null && recordEntity.getData().size() > 0) {
                            for (ExpandServiceRecordEntity.DataBean dataBean : recordEntity.getData()){
                                if (dataBean.getTypeId() == ExpandService.OCR.getTypeId()){
                                    if(DateUtils.isExpire(dataBean.getCurrentTime(), dataBean.getDueTimeStr())){
                                        isDeadline = true;
                                        try {
                                            onResultListener.onFailed(ExpandServiceApplication.getContext().getString(R.string.expand_service_deadline));
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }else {
                                        isDeadline = false;
                                        //未到期则开始识别
                                        runModel();
                                    }
                                    break;
                                }
                            }
                        }else{
                            isDeadline = true;
                            try {
                                onResultListener.onFailed(ExpandServiceApplication.getContext().getString(R.string.expand_service_deadline));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 加载模型
     *
     * @param ocrParams
     */
    public void loadModel(OcrParams ocrParams) {
        this.ocrParams = ocrParams;
        Disposable disposable = Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                emitter.onSuccess(onLoadModel());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean onLoadModel) throws Exception {
                        Log.d(TAG, "模型加载结果：" + onLoadModel);
                        if (onLoadModel) {
                            onLoadModelSuccessed();
                        } else {
                            onLoadModelFailed();
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    /**
     * 开始运行模型，识别图片
     */
    public void runModel() {
        if (targetImg != null && predictor.isLoaded()) {
            predictor.setInputImage(targetImg.getTarget());
        }else {
            return;
        }
        Disposable disposable = Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                emitter.onSuccess(onRunModel());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean onRunModel) throws Exception {
                        Log.d(TAG, "模型运行结果：" + onRunModel);
                        if (onRunModel) {
                            onRunModelSuccess();
                        }else {
                            onRunModelFailed();
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    public boolean onLoadModel() {
        if (ocrParams == null) {
            return false;
        }
        return predictor.init(this,
                ocrParams.getModelPath(),
                ocrParams.getLabelPath(),
                ocrParams.getCpuThreadNum(),
                ocrParams.getCpuPowerMode(),
                ocrParams.getInputColorFormat(),
                ocrParams.getInputShape(),
                ocrParams.getInputMean(),
                ocrParams.getInputStd(),
                ocrParams.getScoreThreshold());
    }

    public boolean onRunModel() {
        return predictor.isLoaded() && predictor.runModel();
    }

    public void onLoadModelSuccessed() {
        // Load test image from path and run model
        Log.d(TAG, "模型加载成功");
    }

    public void onLoadModelFailed() {
        //TODO 模型加载失败处理
    }

    public void onRunModelSuccess() throws RemoteException {
        Log.d(TAG, "Inference time: " + predictor.inferenceTime() + " ms");
        if (onResultListener != null) {
            ArrayList<OcrResultModel> results = predictor.getOcrResult();
            JsonArray jsonArray = new JsonArray();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    OcrResultModel result = results.get(i);
                    StringBuilder sb = new StringBuilder("");
                    sb.append(result.getLabel());
                    sb.append(" ").append(result.getConfidence());
                    sb.append("; Points: ");
                    for (Point p : result.getPoints()) {
                        sb.append("(").append(p.x).append(",").append(p.y).append(") ");
                    }
                    Log.i(TAG, sb.toString());
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", String.valueOf(i + 1));
                    jsonObject.addProperty("word", result.getLabel());
                    jsonArray.add(jsonObject);
                }
            }
            onResultListener.onSuccess(new Gson().toJson(jsonArray));
        }
    }

    public void onRunModelFailed() throws RemoteException {
        Log.e(TAG, "模型运行失败，无法识别图片");
        if (onResultListener != null){
            onResultListener.onFailed("模型运行失败，无法识别图片");
        }
    }

    public OcrParams getDefOcrParams() {
        OcrParams ocrParams = new OcrParams();
        ocrParams.setModelPath(ConstantsUtils.OcrParams.MODEL_PATH_DEFAULT);
        ocrParams.setLabelPath(ConstantsUtils.OcrParams.LABEL_PATH_DEFAULT);
        ocrParams.setImagePath(ConstantsUtils.OcrParams.IMAGE_PATH_DEFAULT);
        ocrParams.setCpuThreadNum(ConstantsUtils.OcrParams.CPU_THREAD_NUM_DEFAULT);
        ocrParams.setCpuPowerMode(ConstantsUtils.OcrParams.CPU_POWER_MODE_DEFAULT);
        ocrParams.setInputColorFormat(ConstantsUtils.OcrParams.INPUT_COLOR_FORMAT_DEFAULT);
        ocrParams.setInputShape(Utils.parseLongsFromString(ConstantsUtils.OcrParams.INPUT_SHAPE_DEFAULT, ","));
        ocrParams.setInputMean(Utils.parseFloatsFromString(ConstantsUtils.OcrParams.INPUT_MEAN_DEFAULT, ","));
        ocrParams.setInputStd(Utils.parseFloatsFromString(ConstantsUtils.OcrParams.INPUT_STD_DEFAULT, ","));
        return ocrParams;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposables != null) {
            for (Disposable d : mDisposables) {
                if (d != null && !d.isDisposed()) {
                    d.dispose();
                }
            }
        }
    }
}
