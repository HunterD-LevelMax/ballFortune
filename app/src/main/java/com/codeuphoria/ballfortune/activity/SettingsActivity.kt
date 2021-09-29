package com.codeuphoria.ballfortune.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codeuphoria.ballfortune.*
import com.codeuphoria.ballfortune.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.versionTV.text = "Сборка " + getString(R.string.app_version)
        binding.pravaTV.text = versionCode
        binding.backFab.setOnClickListener {
            replaceActivity(MainActivity())
        }
        loadSettings()
        setSettings()
    }

    private fun loadSettings(){
        val sound = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            ?.getString("SOUND_SETTINGS", null)

        val vibrate = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            ?.getString("VIBRATE_SETTINGS", null)

        if (sound == "1") {
            binding.soundSwitch.isChecked = true
        }

        if (vibrate == "1") {
            binding.vibrateSwitch.isChecked = true
        }
    }

    private fun setSettings() {
        binding.soundSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            val sharedPreferences: SharedPreferences? =
                this.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            if (isChecked) {
                // The switch is enabled/checked
                editor?.apply { putString("SOUND_SETTINGS", "1") }?.apply()
                showToast(getString(R.string.settings_sound_enabled))
            } else {
                // The switch is disabled
                editor?.apply { putString("SOUND_SETTINGS", "0") }?.apply()
                showToast(getString(R.string.settings_sound_disabled))
            }
        }

        binding.vibrateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val sharedPreferences: SharedPreferences? =
                this.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            if (isChecked) {
                // The switch is enabled/checked
                editor?.apply { putString("VIBRATE_SETTINGS", "1") }?.apply()
                showToast(getString(R.string.settings_vibration_enabled))
            } else {
                // The switch is disabled
                editor?.apply { putString("VIBRATE_SETTINGS", "0") }?.apply()
                showToast(getString(R.string.settings_vibration_disabled))
            }
        }
    }
}