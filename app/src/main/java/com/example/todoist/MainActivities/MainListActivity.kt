package com.example.todoist.MainActivities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.LoginActivity
import com.example.todoist.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var username : String

        try {
            val filename = "username.txt"
            val fis = openFileInput(filename)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            br.close()
            username = sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Failed")
        }

    }

    private fun showToast(string: String?) {
        Toast.makeText(this , string , Toast.LENGTH_SHORT).show()
    }
}