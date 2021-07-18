package com.everis.bootcamp.drinkwater

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.everis.bootcamp.sync.DrinkWaterReminderIntentService
import com.everis.bootcamp.sync.DrinkWaterReminderTask
import com.everis.bootcamp.utils.PreferencesUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Realiza a chamada da função updateWaterCount para já exibir o contador
           caso já possua algum valor */
        updateWaterCount()

        imageview_cup_icon.setOnClickListener {
            incrementWaterHandler()
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    // Atualiza o textview_quantity com o valor da PreferencesUtils.getWaterCount
    fun updateWaterCount() {
        val count = PreferencesUtils.getWaterCount(this)
        textview_quantity.text = "$count"
    }

    fun incrementWaterHandler() {
        // Cria uma intent explícita para acionar o DrinkWaterReminderIntentService
        val intent = Intent(this, DrinkWaterReminderIntentService::class.java)

        // Define a action da Intent com a constant ACTION_INCREMENT_WATER_COUNT
        intent.action = DrinkWaterReminderTask.ACTION_INCREMENT_WATER_COUNT

        // Chama startService e passa a intent como parâmetro
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Chama o método updateWaterCount se o parametro key for igual a constante PrefencesUtils.KEY_WATER_COUNT
        if (key == PreferencesUtils.KEY_WATER_COUNT) {
            updateWaterCount()
        }
    }
}
