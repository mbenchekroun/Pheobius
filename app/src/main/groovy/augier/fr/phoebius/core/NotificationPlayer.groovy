package augier.fr.phoebius.core


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import augier.fr.phoebius.MainActivity
import augier.fr.phoebius.R


/**
 * Helper class for showing and canceling player notifications
 *
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NotificationPlayer
{
    /** Unique identifier for this type of notification. */
    private static final String NOTIFICATION_TAG = "${MainActivity.APP_NAME}#NotifPlayer"
	private static NotificationPlayer INSTANCE

	private NotificationCompat.Builder notification
	private Context context = MainActivity.applicationContext




	private NotificationPlayer()
	{

	}
	/**
	 * Shows or updates the notification
	 *
	 * Shows or update a previously shown notification of
	 * this type, with the given parameters. Make sure to follow the
	 * <a href="https://developer.android.com/design/patterns/notifications.html">
	 * Notification design guidelines</a> when doing so.
	 *
	 * @see #cancel()
	 */
    public void notify(Bitmap picture, String songTitle, String songArtiste,boolean isPlaying){
        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_player_icon)
                .setTicker("${MainActivity.APP_NAME}")
                .setOngoing(true)
                .setLargeIcon(picture)
                .setContentTitle(songTitle)
                .setContentText(songArtiste)

        notification.addAction( generateAction( android.R.drawable.ic_media_previous, "", MusicService.ACTION_PREVIOUS ));
        if(isPlaying){
            notification.addAction( generateAction( android.R.drawable.ic_media_pause, "", MusicService.ACTION_PAUSE ) );
        }else{
            notification.addAction( generateAction( android.R.drawable.ic_media_play, "", MusicService.ACTION_PLAY ) );
        }
        notification.addAction( generateAction( android.R.drawable.ic_media_next, "", MusicService.ACTION_NEXT ) );
        def nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_TAG, 0, notification.build())
    }




    private NotificationCompat.Action generateAction( int icon, String title, String intentAction ) {
        Intent intent = new Intent( context, MusicService.class );
        intent.setAction( intentAction );
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, 0);
        return new NotificationCompat.Action.Builder( icon, title, pendingIntent ).build();
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Bitmap, String, String)}.
     */
    public void cancel()
    {
        def nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_TAG, 0)
    }

	public static NotificationPlayer getInstance()
	{
		if(!INSTANCE)
            INSTANCE = new NotificationPlayer()
		return INSTANCE
	}
}