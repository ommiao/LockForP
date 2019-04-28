package cn.ommiao.lockforp.service;

import android.content.Intent;
import android.service.quicksettings.TileService;

import cn.ommiao.lockforp.activity.ShortcutActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TileLockSevice extends TileService {

    @Override
    public void onClick() {
        Intent it = new Intent(this, ShortcutActivity.class);
        it.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }
}
