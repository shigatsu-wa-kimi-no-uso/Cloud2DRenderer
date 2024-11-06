package me.project.cloud2drenderer.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;
    // 保存手机信息和异常信息
    private final Map<String, String> messages = new HashMap<>();


    public ExceptionHandler() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (!handleException(e)) {
            // 未经过人为处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(t, e);
            }
        } else {
            // 已经人为处理,系统自己退出
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }
    }

    /**
     * 是否人为捕获异常
     *
     * @param e Throwable
     * @return true:已处理 false:未处理
     */
    private boolean handleException(Throwable e) {
        boolean isHandled = false;
        if (e == null) {// 异常是否为空
            return isHandled;
        }

        // 在主线程中弹出提示
       /*new Thread(() -> {
            Looper.prepare();
            Toast.makeText(context, "捕获到异常", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();
        collectErrorMessages();
        saveErrorMessages(e);*/
        return isHandled;
    }

    /**
     * 1.收集错误信息
     */
    private void collectErrorMessages() {
        PackageManager pkgMgr = context.getPackageManager();
        try {
            PackageInfo pkgInfo = pkgMgr.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                String versionName = TextUtils.isEmpty(pkgInfo.versionName) ? "null" : pkgInfo.versionName;
                String versionCode = "" + pkgInfo.getLongVersionCode();
                messages.put("versionName", versionName);
                messages.put("versionCode", versionCode);
            }
            // 通过反射拿到错误信息
            Field[] fields = Build.class.getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    messages.put(field.getName(), Objects.requireNonNull(field.get(null)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2.保存错误信息
     *
     * @param e Throwable
     */
    private void saveErrorMessages(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        String result = writer.toString();
        sb.append(result);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String fileName = "crash-" + time + "-" + System.currentTimeMillis() + ".log";
        // 有无SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/crash/";
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}