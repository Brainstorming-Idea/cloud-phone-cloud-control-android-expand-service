package com.muse.ocrdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyou
 */
public class PicturesActivity extends AppCompatActivity {
    private static final String TAG = "PicturesActivity";
    private GridView picGrid;
    private TextView tv_path;
    private PicGridAdapter picGridAdapter;
    private List<String> images = new ArrayList<>();
//    private MyHandler myHandler = new MyHandler();
//    static class MyHandler extends Handler{
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            if (msg.what == 1){
//                if (picGridAdapter != null){
//                    picGridAdapter.updateImgs(bitmaps);
//                }
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        picGrid = findViewById(R.id.pic_grid);
        picGridAdapter = new PicGridAdapter(this, images);
        picGrid.setAdapter(picGridAdapter);
        tv_path = findViewById(R.id.path_tv);
        tv_path.setText(Environment.getExternalStorageDirectory() + "/Cloudphone/ScreenShot/");
        final File file = new File(Environment.getExternalStorageDirectory() + "/Cloudphone/ScreenShot/");
        if (file.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getPics(file);
                }
            }).start();
        }else {
            Log.e(TAG, "文件夹不存在！");
        }
    }

    private void getPics(File dirFile) {
        // 用于遍历sdcard卡上所有文件的类
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String filePath = file.getPath();
                    if (!file.isHidden()) {
                        if (file.isDirectory()) {
                            getPics(file);
                        } else {
//                            String fileExtension = FileUtils.getFileExtension(filePath);
//                            if ((fileExtension.equalsIgnoreCase("jpg") ||
//                                    fileExtension.equalsIgnoreCase("jpeg") ||
//                                    fileExtension.equalsIgnoreCase("gif") ||
//                                    fileExtension.equalsIgnoreCase("png")) && !file.isHidden()) {
                                // 如果遇到文件则放入数组
//                                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//                                bitmaps.add(bitmap);
                                images.add(filePath);
//                            }
                        }
                    }
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (picGridAdapter != null){
                    picGridAdapter.updateImgs(images);
                }
            }
        });
//        Message msg = myHandler.obtainMessage();
//        msg.what = 1;
//        msg.obj = bitmaps;
//        myHandler.sendMessage(msg);
    }

    static class PicGridAdapter extends BaseAdapter {
        private List<String> images = null;
        private Context context;

        public PicGridAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        public void updateImgs(List<String> images){
            this.images = images;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public String getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_pic_grid, null, false);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView = convertView.findViewById(R.id.item_img);
            Glide.with(context).load(new File(getItem(position))).into(viewHolder.imageView);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"点击了："+position);
                    Intent result = new Intent(context, MainActivity.class);
//                    Bitmap b = (Bitmap) getItem(position);
//                    int bytes = b.getByteCount();
//                    ByteBuffer buffer = ByteBuffer.allocate(bytes);
//                    b.copyPixelsToBuffer(buffer);
//                    result.putExtra("byte_bitmap",buffer.array());
                    Uri uri = Uri.parse(getItem(position));
                    result.setData(uri);
                    Activity activity = (Activity) context;
                    activity.setResult(RESULT_OK,result);
                    activity.finish();
                }
            });
            return convertView;
        }

        static class ViewHolder {
            private ImageView imageView;
        }
    }
}