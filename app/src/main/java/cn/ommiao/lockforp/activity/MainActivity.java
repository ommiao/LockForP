package cn.ommiao.lockforp.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.util.UUID;

import cn.ommiao.lockforp.R;
import cn.ommiao.lockforp.service.LockAccessibilityService;
import cn.ommiao.lockforp.utils.Util;

public class MainActivity extends AppCompatActivity {

    public static final String LOCK_ASSESSIBILITY_START = "LOCK_ASSESSIBILITY_START";
    public static final String LOCK_ASSESSIBILITY_CLOSE = "LOCK_ASSESSIBILITY_CLOSE";

    private ImageView ivState, ivHomeState;

    private LockServiceStateReceiver lockServiceStateReceiver;

    private CardView cvAccessibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initReceiver();
    }

    private void initViews() {
        findViewById(R.id.tv_agree).setOnClickListener(v -> onSwitchClick());
        findViewById(R.id.tv_deny).setOnClickListener(v -> finish());
        findViewById(R.id.cv_switch).setOnClickListener(v -> onSwitchClick());
        findViewById(R.id.cv_more).setOnClickListener(v -> onMoreClick());
        findViewById(R.id.cv_shortcut).setOnClickListener(v -> onShortcutClick());
        findViewById(R.id.cv_home).setOnClickListener(v -> onHomeClick());
        findViewById(R.id.iv_close).setOnClickListener(v -> finish());
        cvAccessibility = findViewById(R.id.cv_accessibility);
        ivState = findViewById(R.id.iv_state);
        ivHomeState = findViewById(R.id.iv_home_state);
        if(isServiceOn()){
            ivState.setImageResource(R.drawable.icon_success);
            cvAccessibility.setVisibility(View.GONE);
        } else {
            ivState.setImageResource(R.drawable.icon_fail);
            cvAccessibility.setVisibility(View.VISIBLE);
        }
        if(LockAccessibilityService.isHomeLongClickStart()){
            ivHomeState.setVisibility(View.VISIBLE);
        } else {
            ivHomeState.setVisibility(View.INVISIBLE);
        }
    }

    private void initReceiver(){
        lockServiceStateReceiver = new LockServiceStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LOCK_ASSESSIBILITY_START);
        filter.addAction(LOCK_ASSESSIBILITY_CLOSE);
        registerReceiver(lockServiceStateReceiver, filter);
    }

    private void onSwitchClick(){
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onMoreClick(){
        Util.shortToast(getString(R.string.no_function));
    }

    private void onShortcutClick(){
        if(isServiceOn()){
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
                Intent shortcutInfoIntent = new Intent(this, ShortcutActivity.class);
                shortcutInfoIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错
                ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(this, UUID.randomUUID().toString())
                        .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher_round))
                        .setShortLabel(getString(R.string.lock))
                        .setIntent(shortcutInfoIntent)
                        .build();

                //当添加快捷方式的确认弹框弹出来时，将被回调
                Intent it = new Intent();
                PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(this, 269, it, PendingIntent.FLAG_IMMUTABLE);
                ShortcutManagerCompat.requestPinShortcut(this, info, shortcutCallbackIntent.getIntentSender());
            }
        } else {
            Util.shortToast(getString(R.string.lock_accessibility_not_start));
        }
    }

    private void onHomeClick(){
        if(isServiceOn()){
            if(LockAccessibilityService.isHomeLongClickStart()){
                LockAccessibilityService.setHomeLongClickStart(false);
                ivHomeState.setVisibility(View.INVISIBLE);
                Util.shortToast(getString(R.string.home_long_click_close));
            } else {
                LockAccessibilityService.setHomeLongClickStart(true);
                ivHomeState.setVisibility(View.VISIBLE);
                Util.shortToast(getString(R.string.home_long_click_start));
            }
        } else {
            Util.shortToast(getString(R.string.lock_accessibility_not_start));
        }
    }

    private boolean isServiceOn() {
        return Util.isLockAccessibilityServiceOn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lockServiceStateReceiver);
    }

    private class LockServiceStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(LOCK_ASSESSIBILITY_START.equals(action)){
                ivState.setImageResource(R.drawable.icon_success);
                cvAccessibility.setVisibility(View.GONE);
            } else if(LOCK_ASSESSIBILITY_CLOSE.equals(action)){
                ivState.setImageResource(R.drawable.icon_fail);
                cvAccessibility.setVisibility(View.VISIBLE);
            }
        }
    }
}
