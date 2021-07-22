package com.cloud.control.expand.service.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by：wangwenbin
 * Date：2019/10/15
 * Explain:文件操作
 */
public class FileUtils {

    private static final String TAG = "FileUtils";
    //系统保存截图的路径
    public static final String SCREENCAPTURE_PATH = "control" + File.separator;

    public static final String SCREENSHOT_NAME = "control_";

    /**
     * 卸载应用
     *
     * @param cmd
     * @return 0 命令执行成功
     * @throws IOException
     */
    public static int execShellCmd(String cmd) throws IOException {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("vc");
//            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            int i = process.waitFor();

            Log.d("SystemManager", "i:" + i);
            return i;
        } catch (Exception e) {
            Log.d("SystemManager", e.getMessage());
            return -1;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

    public static boolean execShell(String cmd) throws IOException {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("vc");
//            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            int i = process.waitFor();
            Log.d("execShell", cmd + ":"+i);
            InputStream ins = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(ins));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            String res = buffer.toString();
            Log.d(TAG,"execShellResult:"+ cmd + ":"+res);
            /*获取错误流*/
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(errorStream));
            StringBuilder errorSb = new StringBuilder();
            String errorLine = "";
            while ((errorLine = errorBuffer.readLine()) != null){
                errorSb.append(errorLine);
            }
            Log.e(TAG,"execShellError:"+cmd + ":"+ errorSb.toString());
            if ("Success".equals(res)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            Log.d("SystemManager", e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }


    public static String getScreenShotsPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return context.getFilesDir().toString();
        }
    }


    /**
     * 应用图标文件路径
     *
     * @param context
     * @return
     */
    public static String getAppIcon(Context context) {
        StringBuffer stringBuffer = new StringBuffer(getScreenShotsPath(context));
        stringBuffer.append(File.separator);
        stringBuffer.append(SCREENCAPTURE_PATH);
        File file = new File(stringBuffer.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return stringBuffer.toString();
    }


    /**
     * 获取截图文件名称，截图地址:/storage/emulated/0/control_X浏览器.jpg
     *
     * @param context
     * @return
     */
    public static String getAppNamePath(Context context, String name) {
        StringBuffer stringBuffer = new StringBuffer(getAppIcon(context));
        stringBuffer.append(SCREENSHOT_NAME);
        stringBuffer.append(name);
        stringBuffer.append(".jpg");
        return stringBuffer.toString();
    }

    /**
     * 保存bitmap文件
     *
     * @param appIcon
     * @param appName
     */
    public static String saveBitmapFile(Context context, Drawable appIcon, String appName) {
        String iconPath = "";
        BitmapDrawable drawable = (BitmapDrawable) appIcon;
        Bitmap mBitmap = drawable.getBitmap();
        iconPath = FileUtils.getAppNamePath(context, appName);
        File picFile = new File(iconPath);
        try {
            picFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(picFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iconPath;
    }

    /**
     * 解析文件大小，返回以B、KB、MB、GB为单位的数据
     *
     * @param appPath
     * @return
     */
    public static String formatFileSize(String appPath) {
        String fileSizeString = "";
        try {
            long fileSize = 0;
            File file = new File(appPath);
            if (file.exists()) {
                fileSize = file.length();
            }
            DecimalFormat df = new DecimalFormat();
            if (fileSize < 1024) {
                fileSizeString = df.format((int) fileSize) + "B";
            } else if (fileSize < (1024 * 1024)) {
                fileSizeString = df.format((int) fileSize / 1024) + "KB";
            } else if (fileSize < (1024 * 1024 * 1024)) {
                fileSizeString = df.format((int) fileSize / (1024 * 1024)) + "MB";
            } else {
                fileSizeString = df.format((int) fileSize / (1024 * 1024 * 1024)) + "GB";
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception e " + e.getMessage());
        }
        return fileSizeString;
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param filePath
     * @return
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 创建文件并写入内容
     *
     * @param filePath
     * @param content
     * @param modify   是否可修改文件内容
     */
    public static void writeFileContent(String filePath, String content, boolean modify) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            //文件是否有内容
            if (file.length() > 0) {
                //是否可修改
                if (modify) {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    Log.d(TAG, "writeFileContent modify success");
                } else {
                    Log.d(TAG, "writeFileContent exits content");
                }
            } else {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                Log.d(TAG, "writeFileContent success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件中的内容
     *
     * @param strFilePath
     * @return
     */
    public static String readFileContent(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d(TAG, "The File doesn't not exist.");
        } else {
            try {
                InputStream inStream = new FileInputStream(file);
                if (inStream != null) {
                    InputStreamReader inputReader = new InputStreamReader(inStream);
                    BufferedReader buffReader = new BufferedReader(inputReader);
                    String line;
                    //分行读取
                    while ((line = buffReader.readLine()) != null) {
                        content += line;
                    }
                    inStream.close();
                }
            } catch (FileNotFoundException e) {
                Log.d(TAG, "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return content;
    }

    /**
     * 文件转base64
     *
     * @param path
     * @return
     */
    public static String fileToBase64(String path) {
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

    /**
     * base64转文件
     *
     * @param base64
     * @param filePath
     */
    public static void base64ToFile(String base64, String filePath) {
        File file = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.decode(base64, Base64.NO_WRAP);
            file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
