package com.codeuphoria.ballfortune

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codeuphoria.ballfortune.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.versionTV.text = "Сборка "

        val versionCode =
            System.getProperty("http.agent").toString() + "\n" + "(с) Все права защищены"
        binding.pravaTV.text = versionCode

        var sound = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            ?.getString("SOUND_SETTINGS", null)

        var vibrate = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            ?.getString("VIBRATE_SETTINGS", null)

        if (sound == "1") {
            binding.soundSwitch.isChecked = true
        }

        if (vibrate == "1") {
            binding.vibrateSwitch.isChecked = true
        }

        binding.backFab.setOnClickListener {
            replaceActivity(MainActivity())
            finish()
        }
        checkSettings()
    }

    private fun checkSettings() {

        binding.soundSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            var sharedPreferences: SharedPreferences? =
                this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            if (isChecked) {
                // The switch is enabled/checked
                editor?.apply { putString("SOUND_SETTINGS", "1") }?.apply()
                showToast("Звук включен")
            } else {
                // The switch is disabled
                editor?.apply { putString("SOUND_SETTINGS", "0") }?.apply()
                showToast("Звук отключен")
            }
        }

        binding.vibrateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            var sharedPreferences: SharedPreferences? =
                this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            if (isChecked) {
                // The switch is enabled/checked
                editor?.apply { putString("VIBRATE_SETTINGS", "1") }?.apply()
                showToast("Вибрация включена")
            } else {
                // The switch is disabled
                editor?.apply { putString("VIBRATE_SETTINGS", "0") }?.apply()
                showToast("Вибрация отключена")
            }
        }
    }

}