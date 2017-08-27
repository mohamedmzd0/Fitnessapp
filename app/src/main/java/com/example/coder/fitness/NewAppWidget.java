package com.example.coder.fitness;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        ArrayList<ProgressModel> models = helper.restorprogress();
        if (models.size() > 0) {
            views.setTextViewText(R.id.name, models.get(models.size() - 1).getName());
            views.setTextViewText(R.id.status, models.get(models.size() - 1).getStatus());
            views.setTextViewText(R.id.date, models.get(models.size() - 1).getDate());
        } else {
            views.setTextViewText(R.id.name, "no progress");
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

