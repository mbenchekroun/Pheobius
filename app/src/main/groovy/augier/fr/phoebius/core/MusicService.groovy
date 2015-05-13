package augier.fr.phoebius.core

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnCompletionListener
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList
import groovy.transform.CompileStatic


/**
 * This class takes care of playing the music and interacting with the controls
 */
@CompileStatic
class MusicService extends Service implements OnPreparedListener,
		OnErrorListener, OnCompletionListener
{
	/**
	 * Our actual music player that will broadcast sound
	 */
	private MediaPlayer mediaPlayer = new MediaPlayer()

	/**
	 * The next music player for gapless playing
	 */
	private MediaPlayer nextMediaPlayer = new MediaPlayer()

	/**
	 * Our manager for the songs
	 */
	private SongList songList

	/**
	 * Our binder (thanks, Captain Obvious !)
	 */
	private final IBinder musicBinder = new MusicBinder()

	/**
	 * Variable to ensure the player is in a validate state
	 */
	private IdleStateHandler idle = new IdleStateHandler()

	private NotificationPlayer notificationPlayer = NotificationPlayer.getInstance()

	@Override
	void onDestroy()
	{
		super.onDestroy()
		notificationPlayer.cancel()
	}

	@Override
	void onCreate()
	{
		super.onCreate()
		songList = SongList.getInstance()
		mediaPlayerInit(mediaPlayer)
		mediaPlayerPrepare(mediaPlayer, songList.currentSong)
		prepareNextPlayer()
	}

	//region Player logic
	/**
	 * Plays a song
	 * @param song Song to be played (see {@link Song ])
	 */
	public void play(Song song)
	{
		if(song == null) stop()
		else
		{
			songList.currentSong = song
			mediaPlayerPrepare(mediaPlayer, songList.currentSong)
			prepareNextPlayer()
			start()
			notificationPlayer.notify(songList.getCoverFor(song.album), song.title, song.album)
		}
	}

	/**
	 * Stops the player
	 */
	public void stop(){ mediaPlayer.stop() }

	/**
	 * Pauses the player
	 */
	public void pause(){ mediaPlayer.pause() }

	/**
	 * Seeks the song currently playing to a given position
	 *
	 * If nithing is playing, does nothing
	 * @param position position to seek to given in milliseconds
	 */
	public void seek(int position){ mediaPlayer.seekTo(position) }

	/**
	 * Starts playing the music
	 */
	public void start(){ mediaPlayer.start() }

	/**
	 * Moves the song playing (or song to be played if the player
	 * is paused) to the previous song.
	 */
	public void playPrevious(){ play(songList.previousSong) }

	/**
	 * Moves the song playing (or song to be played if the player
	 * is paused) to the next song.
	 */
	public void playNext(){ play(songList.nextSong) }
	//endregion


	//region Overrided methods
	@Override
	IBinder onBind(Intent intent){ return musicBinder }

	@Override
	public boolean onUnbind(Intent intent)
	{
		mediaPlayer.stop()
		mediaPlayer.release()
		return false
	}

	/**
	 * Will play the next song as soon as the current on is finished
	 * @param mediaPlayer
	 */
	@Override void onCompletion(MediaPlayer me)
	{
		songList++
		mediaPlayerPrepare(mediaPlayer, songList.currentSong)
		prepareNextPlayer()
		start()
	}

	/** Does nothing  */
	@Override boolean onError(MediaPlayer mediaPlayer, int i, int i2){ return false }

	/** Does nothing */
	@Override void onPrepared(MediaPlayer mediaPlayer){}
	//endregion

	/**
	 * Initializes the player
	 *
	 * This will set the listeners of the player and confgurate it
	 *
	 * @see {@link MusicService#mediaPlayer}
	 */
	private void mediaPlayerInit(MediaPlayer m)
	{
		m.audioStreamType = AudioManager.STREAM_MUSIC
		m.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
		m.onPreparedListener = this
		m.onCompletionListener = this
		m.onErrorListener = this
	}

	private void mediaPlayerPrepare(MediaPlayer m , Song song)
	{
		m.reset()
		if(song.URI != null)
		{
			m.setDataSource(applicationContext, song.URI)
			m.prepare()
			idle.validate()
		}
	}

	private void prepareNextPlayer()
	{
		Song next = songList.nextSong
		if(next != null)
		{
			mediaPlayerPrepare(nextMediaPlayer, next)
			mediaPlayer.nextMediaPlayer = nextMediaPlayer
		}
	}

	//region GET/SET
	/**
	 * Returns the total duration of the song
	 * @return Total duration in milliseconds
	 */
	int getDuration(){ return ready ? mediaPlayer.duration : 0 }

	/**
	 * Inidicates whether our player is actually playing
	 * @return Playing or not
	 */
	boolean isPlaying(){ return ready ? mediaPlayer.playing : false }

	/**
	 * Indicates if the media player is ready to play
	 * @return Reafy or not
	 */
	boolean isReady(){ return idle.ready }

	/**
	 * Returns the elapsed time the song has been playing
	 * @return Position in the playback in milliseconds
	 */
	int getPosition(){ return ready ? mediaPlayer.currentPosition : 0 }
	//endregion

	/**
	 * Our Binder
	 */
	public class MusicBinder extends Binder{ MusicService getService(){ return MusicService.this } }

	private class IdleStateHandler
	{
		private boolean ready = false
		public boolean isReady(){ return ready }
		public void validate(){ ready = true }
	}
}
