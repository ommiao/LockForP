package cn.ommiao.lockforp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.ommiao.lockforp.R;
import cn.ommiao.lockforp.service.LockAccessibilityService;

public class ShortcutActivity extends AppCompatActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        if(Intent.ACTION_CREATE_SHORTCUT.equals(action)){
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.lock));
            returnIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher_round));
            Intent it = new Intent(this, ShortcutActivity.class);
            returnIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, it);
            setResult(RESULT_OK, returnIntent);
        } else {
            Intent it = new Intent(LockAccessibilityService.LOCK_NOW);
            sendBroadcast(it);
        }
        finish();
    }
}
