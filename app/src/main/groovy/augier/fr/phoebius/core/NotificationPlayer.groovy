package augier.fr.phoebius.core;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews;
import augier.fr.phoebius.R;


/**
 * Helper class for showing and canceling player
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NotificationPlayer {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Player";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p/>
     * the notification.
     * <p/>
     * presentation of player notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context,final Bitmap picture,  final String songTitle, final String songSubtTitle) {

        final Resources res = context.getResources();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_VIBRATE)
                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(R.drawable.notification_player_icone)

                        // All fields below this line are optional.

                        // Set ticker text (preview) information for this notification.
                .setTicker(songTitle)
                        // Aset Ongoing so it stays even if the user try to close it.
                .setOngoing(true);

        Notification notif = builder.build();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.


        RemoteViews contentView = new RemoteViews(context.getPackageName(),R.layout.player_notification);
        contentView.setImageViewBitmap(R.id.notificationPlayerImage, picture);
        contentView.setTextViewText(R.id.notificationPlayerMusicTitle1, songTitle);
        contentView.setTextViewText(R.id.notificationPlayerMusicTitle2, songSubtTitle);

        notif.contentView = contentView;
        notif.bigContentView = contentView;

        notify(context, notif);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}