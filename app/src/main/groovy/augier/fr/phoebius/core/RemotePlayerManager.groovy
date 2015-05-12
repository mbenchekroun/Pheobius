package augier.fr.phoebius.core

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
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
public class RemotePlayerManager
{
	private static RemotePlayerManager INSTANCE

    private ArrayList<RemotePlayer> remotePlayers = new ArrayList<RemotePlayer>();


    private RemotePlayerManager(){
        remotePlayers.add(WidgetPlayer.getInstance())
    }


    protected void update(Bitmap picture, String songTitle, String songArtiste,boolean isPlaying){

        for(RemotePlayer e in remotePlayers){
            e.update(picture,songTitle,songArtiste,isPlaying)
        }
        refresh(isPlaying)
    }

    public void refresh(boolean isPlaying){
        for(RemotePlayer e in remotePlayers){
            e.refresh(isPlaying)
        }
    }


	public static RemotePlayerManager getInstance()
	{
		if(!INSTANCE)
            INSTANCE = new RemotePlayerManager()
		return INSTANCE
	}

    protected Context getContext(){ return PhoebiusApplication.context }
}