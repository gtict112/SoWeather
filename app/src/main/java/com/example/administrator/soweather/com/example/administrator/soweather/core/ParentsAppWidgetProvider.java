package com.example.administrator.soweather.com.example.administrator.soweather.core;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.service.AppWidgetUpdateService;

/**
 * Created by Administrator on 2016/11/1.
 */ //桌面小部件

public class ParentsAppWidgetProvider extends AppWidgetProvider {
    private Intent intent;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        intent = new Intent(context, AppWidgetUpdateService.class);
        context.startService(intent);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        intent = new Intent(context, AppWidgetUpdateService.class);
        context.stopService(intent);
        super.onDeleted(context, appWidgetIds);
    }
}