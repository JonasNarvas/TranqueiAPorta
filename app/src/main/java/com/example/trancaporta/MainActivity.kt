package com.example.trancaporta

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.trancaporta.ui.theme.TrancaPortaTheme
import java.util.*
import java.text.SimpleDateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Date
import java.text.DateFormat
import kotlin.collections.ArrayList


class MainActivity : ComponentActivity() {
    private lateinit var dateTimeTextView: TextView
    private lateinit var allLogsTextView: TextView
    private val logTimes: MutableList<String> = mutableListOf()
    private val gson = Gson()
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LogTimesPrefs"
    private val KEY_LOG_TIMES = "LogTimes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateTimeTextView = findViewById(R.id.dateandtime)
        allLogsTextView = findViewById(R.id.allLogsTextView)
        val logButton: Button = findViewById(R.id.botaodatetime)
        val readJsonButton: Button = findViewById(R.id.lerHistoricoJson)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadLogTimes()

        logButton.setOnClickListener {
            logDateTime()
        }

        readJsonButton.setOnClickListener{
            readJson()
        }

    }
    private fun logDateTime() {
        val currentDateTime =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            logTimes.add(currentDateTime)
        val json = gson.toJson(logTimes)
        dateTimeTextView.text = "Trancou! \n $currentDateTime"
    }

    private fun readJson() {
        val json = gson.toJson(logTimes) // Get the JSON string
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        val times: List<String> = gson.fromJson(json, listType)
        val allTimes = times.joinToString(separator = "\n")
        allLogsTextView.text = allTimes
    }

    private fun loadLogTimes() {
        val json = sharedPreferences.getString(KEY_LOG_TIMES, "")
        if (!json.isNullOrEmpty()) {
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            logTimes.addAll(gson.fromJson(json, listType))
        }
    }
}
