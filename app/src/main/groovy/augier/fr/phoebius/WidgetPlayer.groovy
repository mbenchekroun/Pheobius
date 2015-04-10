package augier.fr.phoebius

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.provider.SyncStateContract
import android.util.Log
import android.widget.ImageView
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
public class WidgetPlayer extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_player);

        //create the bitmap element
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.example_appwidget_preview);
        //push the bitmap element to the Container
        views.setImageViewBitmap(R.id.WidgetPlayerImage,bm);


        views.setTextViewText(R.id.widgetPlayerMusicTitle1,"Music 1");
        views.setTextViewText(R.id.widgetPlayerMusicTitle2,"Subtitle 1");
        //commit
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}


