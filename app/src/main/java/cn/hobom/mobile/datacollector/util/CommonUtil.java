package cn.hobom.mobile.datacollector.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;

import cn.hobom.mobile.datacollector.CollectorApplication;


/**
 * Created by hsj on 2016/5/28.
 */
public class CommonUtil {
    private static String TAG = "CommonUtil";

    private static Toast mToast;
    private static Toast toastDIY;

    private static Handler handler;

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return true, 空字符串;false,非空
     */
    public static boolean isEmpty(String s) {
        if (null == s) {
            return true;
        }
        if (s.length() == 0) {
            return true;
        }
        if (s.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 去除空格
     *
     * @param src 原字符串
     * @return 去除空格后的字符串
     */
    public static String clearSpace(String src) {
        return src.replaceAll(" ", "");
    }

    /**
     * 显示toast消息
     *
     * @param toast 消息内容
     */
    public static void showToast(final String toast) {
        try {
            showToastOnUiThread(toast, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示toast消息在屏幕中间
     *
     * @param toast 消息内容
     */
    public static void showToastMidScreen(final String toast) {
        showToastOnUiThread(toast, -2);
    }

    /**
     * 显示自定义toast
     *
     * @param context
     * @param layoutID 自定义view布局id
     * @param textID   自定义textView的id
     * @param text     自定义的文本信息
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastDIY(Context context, int layoutID, int textID,
                                    String text, int gravity, int xOffset, int yOffset) {
        if (toastDIY == null) {
            toastDIY = new Toast(context);
        }
        View view = View.inflate(context, layoutID, null);
        toastDIY.setView(view);
        TextView tv = (TextView) view.findViewById(textID);
        tv.setText(text);
        toastDIY.setGravity(gravity, xOffset, yOffset);
        toastDIY.show();
    }

    /**
     * 显示toast消息
     *
     * @param toast    消息内容
     * @param duration 持续显示时间
     */
    public static void showToast(final String toast, final int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(CollectorApplication.mContext, toast, Toast.LENGTH_SHORT);
        }
        if (duration == -2) {
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, mToast.getYOffset());
        } else {
            mToast.setGravity(Gravity.BOTTOM, 0, mToast.getYOffset());
        }
        if (duration <= 0) {
            mToast.setDuration(Toast.LENGTH_SHORT);
        } else {
            mToast.setDuration(duration);
        }
        mToast.setText(toast);
        mToast.show();
    }

    public static void showToastOnUiThread(final String toast, final int duration) {
        if (handler == null) {
            handler = new Handler(CollectorApplication.mContext.getMainLooper());
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                showToast(toast, duration);
            }
        });
    }


    /**
     * 获取md5字符串
     *
     * @param s
     * @return
     */
    public static String getMd5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String md5 = new String(str);
            Log.i(TAG, md5);
            return md5.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 安全删除
     *
     * @param file
     * @return
     */
    public static boolean safeDeleteFile(File file) {
        if (file != null && file.exists()) {
            try {
                File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
                boolean isRename = file.renameTo(to);
                if (isRename) {
                    return to.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    public static boolean delete(File file) {
        if (file.isFile()) {
            safeDeleteFile(file);
            return true;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                safeDeleteFile(file);
                return true;
            }

            for (File childFile : childFiles) {
                safeDeleteFile(childFile);
            }
            return safeDeleteFile(file);
        }
        return false;
    }
}
