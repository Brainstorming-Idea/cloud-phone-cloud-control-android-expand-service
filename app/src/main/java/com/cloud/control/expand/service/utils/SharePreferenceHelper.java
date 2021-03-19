package com.cloud.control.expand.service.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharePreferenceHelper {
    private static SharePreferenceHelper instance;
    private static final String SP_NAME_DEFAULT = "expand_server_sp_name";
    private static Editor editor;

    private Context context;
    private SharedPreferences preferences;

    public static SharePreferenceHelper getInstance(Context c) {
        if (instance == null) {
            instance = new SharePreferenceHelper(c);
        }
        return instance;
    }

    private SharePreferenceHelper(Context c) {
        context = c.getApplicationContext();
        open(SP_NAME_DEFAULT);
        editor = preferences.edit();
    }

    public void open(String name) {
        open(name, 0);
    }

    public void open(String name, int version) {
        String fileName = name + "_" + version;
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void putBoolean(String key, Boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void putLong(String key, Long value) {
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
        editor.commit();
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    public void putInt(String key, Integer value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void putFloat(String key, Float value) {
        Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }


    /**
     * 对应的get方法为 {@link #get(String)}
     *
     * @param key
     * @param value 先用Base64编码再保存
     */
    public void put(String key, Object value) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            oos.flush();
            byte[] data = baos.toByteArray();
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
            putString(key, base64);
            oos.close();
            baos.close();
        } catch (Exception t) {
            t.printStackTrace();
        }
    }

    /**
     * 对应的put方法为 {@link #put(String, Object)}
     *
     * @param key
     * @return 返回Base64解码后的字符
     */
    public Object get(String key) {
        try {
            String base64 = getString(key);
            byte[] data = Base64.decode(base64, Base64.NO_WRAP);

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object value = ois.readObject();
            ois.close();
            bais.close();
            return value;
        } catch (Exception t) {
            t.printStackTrace();
        }
        return null;
    }

    public boolean putObject(String key, Object object){
        editor.putString(key, new Gson().toJson(object));
        editor.apply();
        return editor.commit();
    }

    public <T> T getObject(String key, Class<T> object){
        String s = preferences.getString(key,null);
        if (!TextUtils.isEmpty(s)){
            return new Gson().fromJson(s, object);
        }
        return null;
    }

    public void remove(String key) {
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
        editor.commit();

    }

    public void clean() {
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

    }
}
