package com.example.forwaiters.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.forwaiters.R
import com.example.forwaiters.database.ForWaitersDatabase
import com.example.forwaiters.database.Piano
import com.example.forwaiters.others.InfoOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var buttonFloorOne: Button
    private lateinit var buttonFloorTwo: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initSharedPreferences()

        if (isFirstLaunch()) {
            showLoadingDialogAndRestart()
        } else {
            loadInitialData()
        }

        setupButtonListeners()
    }

    private fun initViews() {
        buttonFloorOne = findViewById(R.id.buttonFloorOne)
        buttonFloorTwo = findViewById(R.id.buttonFloorTwo)
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }

    private fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }

    private fun setupButtonListeners() {
        buttonFloorOne.setOnClickListener {
            navigateToFloorActivity(Piano("pianoUno", "PIANO-1"))
        }
        buttonFloorTwo.setOnClickListener {
            navigateToFloorActivity(Piano("pianoDue", "PIANO-2"))
        }
    }

    private fun navigateToFloorActivity(piano: Piano) {
        InfoOrder.piano = piano
        val intent = Intent(this, FloorActivity::class.java)
        startActivity(intent)
    }

    private fun showLoadingDialogAndRestart() {
        val alertDialog = createLoadingDialog()
        alertDialog.show()

        lifecycleScope.launch(Dispatchers.IO) {
            initializeDatabase()

            setFirstLaunchCompleted()

            withContext(Dispatchers.Main) {
                delay(30000)
                alertDialog.dismiss()
                restartApp()
            }
        }
    }

    private fun createLoadingDialog(): AlertDialog {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_layout, null)
        return AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
    }

    private suspend fun initializeDatabase() {
        val podkovaDatabase = ForWaitersDatabase.getInstance(applicationContext)
        InfoOrder.listaCategorie = podkovaDatabase.categoriaDao.ottieniTutteLeCategorie()
    }

    private fun setFirstLaunchCompleted() {
        sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            val componentName = it.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }

    private fun loadInitialData() {
        lifecycleScope.launch(Dispatchers.IO) {
            initializeDatabase()
        }
    }
}
