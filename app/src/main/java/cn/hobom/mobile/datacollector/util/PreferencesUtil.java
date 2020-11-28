package cn.hobom.mobile.datacollector.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Iterator;
import java.util.Set;
import cn.hobom.mobile.datacollector.CollectorApplication;

/**
 * 本地存储工具类{@link SharedPreferences},关联账号
 *
 * @time 2014/11/15
 */
public class PreferencesUtil {

    private static String TAG = "PreferencesUtil";
    // 全局单例
    private static PreferencesUtil instance;

    /**
     * 通用文件存储名，用于保存与账号无关的通用信息
     */
    public static final String UNIVERSAL_STORAGE = "accelerate_Data";

    private Context context;

    private PreferencesUtil(Context context) {
        this.context = context;
    }

    public static PreferencesUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtil(context);
        }
        return instance;
    }

    public static PreferencesUtil getInstance() {
        if (instance == null) {
            instance = new PreferencesUtil(CollectorApplication.mContext);
        }
        return instance;
    }

    /**
     * 获取通用的{@link SharedPreferences}
     *
     * @return
     */
    private static SharedPreferences getUniversalPreferences() {
        SharedPreferences preferences = CollectorApplication.mContext.getSharedPreferences(
                UNIVERSAL_STORAGE, Context.MODE_PRIVATE);
        return preferences;
    }


    /**
     * 删除与key关联的数据
     *
     * @param key
     */
    public static void removeValue(String key){
        Editor editor = getUniversalPreferences().edit();
        editor.remove(key);
        editor.commit();
    }


    public static String getSettingString(final String key, final String defaultValue) {
        return getUniversalPreferences().getString(key, defaultValue);
    }

    public static boolean getSettingBoolean(final String key,
                                            final boolean defaultValue) {
        return getUniversalPreferences().getBoolean(key, defaultValue);
    }

    public static int getSettingInt(final String key, final int defaultValue) {
        return getUniversalPreferences().getInt(key, defaultValue);
    }

    public static long getSettingLong(final String key, final long defaultValue) {
        return getUniversalPreferences().getLong(key, defaultValue);
    }

    public static boolean setSettingString(final String key, final String value) {
        final Editor settingsEditor = getUniversalPreferences().edit();

        settingsEditor.putString(key, value);

        return settingsEditor.commit();
    }

    public static boolean setSettingBoolean(final String key, final boolean value) {
        final Editor settingsEditor = getUniversalPreferences().edit();
        settingsEditor.putBoolean(key, value);
        return settingsEditor.commit();
    }

    public static boolean setSettingInt(final String key, final int value) {
        final Editor settingsEditor = getUniversalPreferences().edit();
        settingsEditor.putInt(key, value);
        return settingsEditor.commit();
    }

    public static boolean setSettingLong(final String key, final long value) {
        final Editor settingsEditor = getUniversalPreferences().edit();
        settingsEditor.putLong(key, value);
        return settingsEditor.commit();
    }
}
