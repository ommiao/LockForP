package cn.ommiao.lockforp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import cn.ommiao.lockforp.R;
import cn.ommiao.lockforp.activity.ShortcutActivity;

public class BlankLockWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_blank_lock);
        Intent it = new Intent(context, ShortcutActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 269, it, 0);
        views.setOnClickPendingIntent(R.id.lock, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
