package cn.ommiao.lockforp.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.view.accessibility.AccessibilityEvent;

import cn.ommiao.lockforp.activity.MainActivity;
import cn.ommiao.lockforp.activity.ShortcutActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class LockAccessibilityService extends AccessibilityService {

    private static final String PACKAGE_SYSTEMUI = "com.android.systemui";

    public static final String LOCK_NOW = "LOCK_NOW";

    private static boolean HOME_LONG_CLICK_START = false;

    private static SharedPreferences preferences;

    private String homeString;

    private LockActionReceiver lockActionReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        homeString = getVirtualNavigationKey(this, "accessibility_home", PACKAGE_SYSTEMUI);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        HOME_LONG_CLICK_START = preferences.getBoolean("HOME_LONG_CLICK_START", false);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Intent notify = new Intent(MainActivity.LOCK_ASSESSIBILITY_START);
        sendBroadcast(notify);
        lockActionReceiver = new LockActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LOCK_NOW);
        registerReceiver(lockActionReceiver, filter);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(!HOME_LONG_CLICK_START){
            return;
        }
        if(PACKAGE_SYSTEMUI.contentEquals(event.getPackageName()) &&
                homeString.contentEquals(event.getContentDescription())){
            Intent intent = new Intent(this, ShortcutActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent notify = new Intent(MainActivity.LOCK_ASSESSIBILITY_CLOSE);
        sendBroadcast(notify);
        unregisterReceiver(lockActionReceiver);
    }

    public static boolean isHomeLongClickStart(){
        return LockAccessibilityService.HOME_LONG_CLICK_START;
    }

    public static void setHomeLongClickStart(boolean start){
        LockAccessibilityService.HOME_LONG_CLICK_START = start;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("HOME_LONG_CLICK_START", start);
        editor.apply();
    }

    private void lockNow(){
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
    }

    private String getVirtualNavigationKey(Context context, String name, String packageName)
    {
        try
        {
            Resources packageManager = context.getPackageManager().getResourcesForApplication(packageName);
            return packageManager.getString(packageManager.getIdentifier(name, "string", packageName));
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private class LockActionReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(LOCK_NOW.equals(action)){
                lockNow();
            }
        }
    }

}
