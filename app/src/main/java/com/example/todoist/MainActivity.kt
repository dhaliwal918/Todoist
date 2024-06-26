package com.example.todoist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.MainActivities.MainListActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val filename = "username.txt"
        try {
            val fis = openFileInput(filename)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            br.close()
            val savedUsername = sb.toString()

            Handler().postDelayed({
                val intent = Intent(this , MainListActivity::class.java)
                startActivity(intent)
                finish()
            } , 2500 )
        } catch (e: IOException) {
            e.printStackTrace()
            Handler().postDelayed({
                val intent = Intent(applicationContext ,LoginActivity::class.java )
                startActivity(intent)
                finish()
            } , 2500 )
        }





    }
}