package cn.ommiao.lockforp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ommiao.lockforp.App;
import cn.ommiao.lockforp.R;
import cn.ommiao.lockforp.service.LockAccessibilityService;

public class Util {

    @SuppressLint("StaticFieldLeak")
    private static final Context context = App.getContext();

    public static boolean isLockAccessibilityServiceOn() {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + LockAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void shortToast(String content){
        Toast toast = new Toast(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
        TextView textView = view.findViewById(R.id.toast_text);
        textView.setText(content);
        toast.setView(view);
        toast.show();
    }

}
