package com.codeuphoria.ballfortune

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.animation.AnimationUtils
import androidx.core.app.ShareCompat
import com.codeuphoria.ballfortune.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    var countShake: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val shake = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            .getString("SHAKE_COUNTER", null)

        if (shake != null) {
            countShake = shake.toInt()
            binding.progressBar.progress = countShake
        }


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
            val sharedPreferences: SharedPreferences? =
                this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            editor?.apply { putString("SHAKE_COUNTER", countShake.toString()) }?.apply()

            replaceActivity(SettingsActivity())
        }

        startAnim()
        initSensor()
    }

    override fun onResume() {
        super.onResume()
        startAnim()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager!!.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences: SharedPreferences? =
            this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

        editor?.apply { putString("SHAKE_COUNTER", countShake.toString()) }?.apply()
    }

    override fun onPause() { // Add the following line to unregister the Sensor Manager onPause
        mSensorManager!!.unregisterListener(mShakeDetector)
        val sharedPreferences: SharedPreferences? =
            this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

        editor?.apply { putString("SHAKE_COUNTER", countShake.toString()) }?.apply()

        super.onPause()
    }

    private fun initSensor() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener(object : ShakeDetector.OnShakeListener {

            override fun onShake(count: Int) {
                startMagic()

//                Toast.makeText(this@MainActivity, count.toString(), Toast.LENGTH_SHORT).show()
            }
        })
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

        countShake++

        binding.progressBar.progress = countShake


        //show advertising
        if (countShake == 10) {
            showToast("10 трясок!")
            countShake = 0

            val sharedPreferences: SharedPreferences? =
                this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            editor?.apply { putString("SHAKE_COUNTER", "0") }?.apply()






        }

        binding.textTV.text = getAnswer()
        binding.magicBallImageView.isClickable = false
        binding.textTV.isClickable = false

        val handler = android.os.Handler()
        handler.postDelayed(
            {
                binding.magicBallImageView.isClickable = true
                binding.textTV.isClickable = true

            }, 1000
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

}


