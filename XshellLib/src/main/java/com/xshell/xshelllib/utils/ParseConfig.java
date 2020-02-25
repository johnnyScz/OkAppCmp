package com.xshell.xshelllib.utils;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * appconfig.xml解析器
 *
 * @author zzy
 */
public class ParseConfig {
    private static ParseConfig instance;
    private static Map<String, String> configInfo = new HashMap<String, String>();

    public static ParseConfig getInstance(Context context) {
        if (instance == null) {
            instance = new ParseConfig();
            parse(context);
        }
        return instance;
    }

    private ParseConfig() {

    }

    public Map<String, String> getConfigInfo() {
        return configInfo;
    }

    public static void parse(Context action) {
        int id = action.getResources().getIdentifier("appconfig", "xml", action.getPackageName());
        parse(action.getResources().getXml(id));

    }

    private static void parse(XmlPullParser xml) {
        int eventType = -1;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                handleStartTag(xml);
            } else if (eventType == XmlPullParser.END_TAG) {
                // handleEndTag(xml);
            }
            try {
                eventType = xml.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleStartTag(XmlPullParser xml) {
        String strNode = xml.getName();
        if (strNode.equals("wxapp-id"))
            configInfo.put("wxapp-id", xml.getAttributeValue(null, "id"));
        else if (strNode.equals("wxapp-secret"))
            configInfo.put("wxapp-secret", xml.getAttributeValue(null, "secret"));
        else if (strNode.equals("app-update-time"))
            configInfo.put("app-update-time", xml.getAttributeValue(null, "time"));
        else if (strNode.equals("html-update-time"))
            configInfo.put("html-update-time", xml.getAttributeValue(null, "time"));
        else if (strNode.equals("class-home"))
            configInfo.put("class-home", xml.getAttributeValue(null, "path"));
        else if (strNode.equals("xversion-html-name"))
            configInfo.put("xversion-html-name", xml.getAttributeValue(null, "value"));
        else if (strNode.equals("xversion-app-name"))
            configInfo.put("xversion-app-name", xml.getAttributeValue(null, "value"));
        else if (strNode.equals("xversion-apk-name"))
            configInfo.put("xversion-apk-name", xml.getAttributeValue(null, "value"));
        else if (strNode.equals("xversion-update-url"))
            configInfo.put("xversion-update-url", xml.getAttributeValue(null, "value"));
        else if (strNode.equals("xversion-download-url"))
            configInfo.put("xversion-download-url", xml.getAttributeValue(null, "value"));
        else if (strNode.equals("xversion-update-content-url"))
            configInfo.put("xversion-update-content-url", xml.getAttributeValue(null, "value"));

    }
}
