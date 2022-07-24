package expo.modules.alarmemodule

import android.content.Context

internal class Sound(context: Context) {
    private val audioManager: AudioManager
    private val userVolume: Int
    private val mediaPlayer: MediaPlayer
    private val vibrator: Vibrator
    private val context: Context

    init {
        this.context = context
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        userVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
        mediaPlayer = MediaPlayer()
    }

    fun play(sound: String) {
        val soundUri: Uri = getSoundUri(sound)
        playSound(soundUri)
        startVibration()
    }

    fun stop() {
        try {
            if (mediaPlayer.isPlaying()) {
                stopSound()
                stopVibration()
                mediaPlayer.release()
            }
        } catch (e: IllegalStateException) {
            Log.d(TAG, "Sound has probably been released already")
        }
    }

    private fun playSound(soundUri: Uri) {
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.setScreenOnWhilePlaying(true)
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
                mediaPlayer.setDataSource(context, soundUri)
                mediaPlayer.setVolume(100, 100)
                mediaPlayer.setLooping(true)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to play sound", e)
        }
    }

    private fun stopSound() {
        try {
            // reset the volume to what it was before we changed it.
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, userVolume, AudioManager.FLAG_PLAY_SOUND)
            mediaPlayer.stop()
            mediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "ringtone: " + e.getMessage())
        }
    }

    private fun startVibration() {
        vibrator.vibrate(DEFAULT_VIBRATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            vibrator.vibrate(500)
        }
        val pattern = longArrayOf(0, 100, 1000)

        // The '0' here means to repeat indefinitely
        // '0' is actually the index at which the pattern keeps repeating from (the start)
        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
        vibrator.vibrate(pattern, 0)
    }

    private fun stopVibration() {
        vibrator.cancel()
    }

    private fun getSoundUri(soundName: String): Uri {
        var soundName = soundName
        val soundUri: Uri
        if (soundName.equals("default")) {
            soundUri = Settings.System.DEFAULT_RINGTONE_URI
        } else {
            val resId: Int
            if (context.getResources().getIdentifier(soundName, "raw", context.getPackageName()) !== 0) {
                resId = context.getResources().getIdentifier(soundName, "raw", context.getPackageName())
            } else {
                soundName = soundName.substring(0, soundName.lastIndexOf('.'))
                resId = context.getResources().getIdentifier(soundName, "raw", context.getPackageName())
            }
            soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId)
        }
        return soundUri
    }

    companion object {
        private const val TAG = "AlarmSound"
        private const val DEFAULT_VIBRATION: Long = 100
    }
}