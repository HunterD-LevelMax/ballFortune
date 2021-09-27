package com.codeuphoria.ballfortune

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.animation.AnimationUtils
import androidx.core.app.ShareCompat
import com.codeuphoria.ballfortune.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textTV.setOnClickListener {
            startMagic()
        }
        binding.magicBallImageView.setOnClickListener {
            startMagic()
        }

        binding.shareFab.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Поделиться приложением с друзьями")
                .setText("http://play.google.com/store/apps/details?id=" + this.packageName)
                .startChooser()
        }

        binding.settingsFab.setOnClickListener {
            replaceActivity(SettingsActivity())
        }

        startAnim()
    }

    private fun setSettings() {
        val sound = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            .getString("SOUND_SETTINGS", null)
        val vibrate = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            .getString("SOUND_SETTINGS", null)


        if (sound == null || vibrate == null) {
            val sharedPreferences: SharedPreferences? =
                this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            editor?.apply { putString("SOUND_SETTINGS", "1") }?.apply()
            editor?.apply { putString("VIBRATE_SETTINGS", "1") }?.apply()
        }
    }

    private fun startAnim() {
        binding.magicBallImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
        binding.textTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
        binding.textTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wave))
    }

    private fun startMagic() {
        setSettings()
        startAnim()
        binding.textTV.text = getAnswer()

        binding.magicBallImageView.isClickable = false
        binding.textTV.isClickable = false

        val handler = android.os.Handler()
        handler.postDelayed(
            {
                binding.magicBallImageView.isClickable = true
                binding.textTV.isClickable = true

            }, 2000
        )

        val sound = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            .getString("SOUND_SETTINGS", null)
        val vibrate = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            .getString("VIBRATE_SETTINGS", null)

        if (sound == "1" || sound == null) {
            val mediaPlayer = MediaPlayer.create(this, R.raw.shake_sound)
            mediaPlayer.start()
        }

        if (vibrate == "1" || vibrate == null) {
            //есть вибрация
            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibratorService.vibrate(250)
        }
    }

    private fun getAnswer(): String {
        return resources.getStringArray(R.array.answers)[randomNumber()]
    }

    private fun randomNumber(): Int {
        val size = resources.getStringArray(R.array.answers).size - 1
        return (0..size).random()
    }

    override fun onBackPressed() {
        //don't exit
    }

    override fun onResume() {
        super.onResume()
        startAnim()
    }
}


