package com.example.meteo.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.meteo.R

class WeatherWidgetReceiver : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.weather_widget)
            views.setTextViewText(R.id.widgetTemp, "--°C")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
