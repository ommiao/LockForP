package cn.ommiao.lockforp.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.ommiao.lockforp.App;
import cn.ommiao.lockforp.R;
import cn.ommiao.lockforp.service.LockAccessibilityService;

public class Util {

    @SuppressLint("StaticFieldLeak")
    private static final Context context = App.getContext();

    public static boolean isLockAccessibilityServiceOn() {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(LockAccessibilityService.class.getName()))
                return true;
        }
        return false;
    }

    public static void shortToast(String content){
        toast(content, Toast.LENGTH_SHORT);
    }

    public static void toast(String content, int duration){
        Toast toast = new Toast(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
        TextView textView = view.findViewById(R.id.toast_text);
        textView.setText(content);
        toast.setView(view);
        toast.setDuration(duration);
        toast.show();
    }

}
