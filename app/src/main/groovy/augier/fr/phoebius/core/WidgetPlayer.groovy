package augier.fr.phoebius.core

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.PlayerWidget
import augier.fr.phoebius.R
import groovy.transform.CompileStatic

/**
 * Helper class for showing and canceling player notifications
 *
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
@CompileStatic
public class WidgetPlayer extends RemotePlayer
{
	private static WidgetPlayer INSTANCE

    private AppWidgetManager appWidgetManager

    private WidgetPlayer(){
        super();
        appWidgetManager = AppWidgetManager.getInstance(context);
    }

    @Override
    public void refresh(boolean isPlaying){
        updatePlayButton(isPlaying);
        ComponentName thisWidget = new ComponentName(context, PlayerWidget.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

	public static WidgetPlayer getInstance()
	{
		if(!INSTANCE)
            INSTANCE = new WidgetPlayer()
		return INSTANCE
	}

    protected Context getContext(){ return PhoebiusApplication.context }
}