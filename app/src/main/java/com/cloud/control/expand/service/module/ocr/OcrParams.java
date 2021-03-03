package com.cloud.control.expand.service.module.ocr;

/**
 * @author wangyou
 * @desc: OCR识别的一些参数配置
 * @date :2021/2/25
 */
public class OcrParams {
    protected String modelPath = "";
    protected String labelPath = "";
    protected String imagePath = "";
    protected int cpuThreadNum = 1;
    protected String cpuPowerMode = "";
    protected String inputColorFormat = "";
    protected long[] inputShape = new long[]{};
    protected float[] inputMean = new float[]{};
    protected float[] inputStd = new float[]{};
    protected float scoreThreshold = 0.1f;
    private String currentPhotoPath;

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getLabelPath() {
        return labelPath;
    }

    public void setLabelPath(String labelPath) {
        this.labelPath = labelPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCpuThreadNum() {
        return cpuThreadNum;
    }

    public void setCpuThreadNum(int cpuThreadNum) {
        this.cpuThreadNum = cpuThreadNum;
    }

    public String getCpuPowerMode() {
        return cpuPowerMode;
    }

    public void setCpuPowerMode(String cpuPowerMode) {
        this.cpuPowerMode = cpuPowerMode;
    }

    public String getInputColorFormat() {
        return inputColorFormat;
    }

    public void setInputColorFormat(String inputColorFormat) {
        this.inputColorFormat = inputColorFormat;
    }

    public long[] getInputShape() {
        return inputShape;
    }

    public void setInputShape(long[] inputShape) {
        this.inputShape = inputShape;
    }

    public float[] getInputMean() {
        return inputMean;
    }

    public void setInputMean(float[] inputMean) {
        this.inputMean = inputMean;
    }

    public float[] getInputStd() {
        return inputStd;
    }

    public void setInputStd(float[] inputStd) {
        this.inputStd = inputStd;
    }

    public float getScoreThreshold() {
        return scoreThreshold;
    }

    public void setScoreThreshold(float scoreThreshold) {
        this.scoreThreshold = scoreThreshold;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }
}