package augier.fr.phoebius.core

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
public abstract class RemotePlayer
{
	protected RemoteViews remoteViews

    protected RemotePlayer(){
        remoteViews = new RemoteViews(context.packageName, R.layout.player_widget)
        remoteViews.setOnClickPendingIntent(R.id.btnPrevious,generateAction(MusicService.ACTION_PREVIOUS))
        remoteViews.setOnClickPendingIntent(R.id.btnBackward,generateAction(MusicService.ACTION_REWIND))
        remoteViews.setOnClickPendingIntent(R.id.btnPlayPause,generateAction(MusicService.Action_Play_Pause))
        remoteViews.setOnClickPendingIntent(R.id.btnForward,generateAction(MusicService.ACTION_FAST_FORWARD))
        remoteViews.setOnClickPendingIntent(R.id.btnNext,generateAction(MusicService.ACTION_NEXT))
    }

    private PendingIntent generateAction(String intentAction){
        Intent intent = new Intent( context, MusicService.class );
        intent.setAction( intentAction );
        return PendingIntent.getService(context, 1, intent, 0);
    }

    protected void update(Bitmap picture, String songTitle, String songArtiste,boolean isPlaying){
        remoteViews.setImageViewBitmap(R.id.notifAlbumCover, picture)
        remoteViews.setTextViewText(R.id.notifSongTitleLabel, songTitle)
        remoteViews.setTextViewText(R.id.notifSongArtistLabel, songArtiste)
        refresh(isPlaying);
    }

    protected void updatePlayButton(boolean isPlaying){
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.btnPlayPause,R.drawable.btn_pause)
        }else{
            remoteViews.setImageViewResource(R.id.btnPlayPause,R.drawable.btn_play)
        }
    }

    public void refresh(boolean isPlaying){}

	public static RemotePlayer getInstance(){}

	protected Context getContext(){ return PhoebiusApplication.context }
}