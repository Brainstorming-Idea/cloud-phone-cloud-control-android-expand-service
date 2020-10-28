package com.cloud.control.expand.service.utils;

import android.text.TextUtils;
import android.util.Xml;

import com.cloud.control.expand.service.log.KLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wangwenbin
 * Date: 2020/5/19
 * Describe: XmlPullParser解析XML文件
 */
public class XMLParser {

    public static String parseXMLForConfigFile(String xmlPath) {
        FileInputStream xmlReader = null;
        String hostUrl = "";

        try {
            xmlReader = new FileInputStream(xmlPath);
            XmlPullParser parser = Xml.newPullParser();

            parser.setInput(xmlReader, "utf-8");
            int type = parser.getEventType();

            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (TextUtils.equals(parser.getName(), "string")) {
                            String attrValue = parser.getAttributeValue(0);
                            String attrValueText = parser.nextText();
                            KLog.e("attrValue = " + attrValue);
                            KLog.e("attrValue Text= " + attrValueText);
                            if (attrValue.equals("KEY_WEBSOCKET_URL")) {
                                hostUrl = attrValueText;
                            }
                        }
                        break;
                }

                type = parser.next();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xmlReader != null) {
                try {
                    xmlReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return hostUrl;
    }

}
