package com.example.todoist.MainActivities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.AddTask.AddTaskActivity
import com.example.todoist.LoginActivity
import com.example.todoist.R
import com.example.todoist.databinding.ActivityMainListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainListBinding
    private lateinit var database : DatabaseReference

    private lateinit var titleArrayList : ArrayList<String>
    private lateinit var descriptionArrayList : ArrayList<String>
    private lateinit var priorityArrayList : ArrayList<String>
    private lateinit var timeArrayList : ArrayList<String>
    private lateinit var dateArrayList : ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // len is used to find

        database = FirebaseDatabase.getInstance().getReference("tasks")
        val idsList = readIdsFromFile(this , "all_ids.txt")
        var i = 0
        while(i!=customLen(idsList)){
            database.child(getUsername()).child(idsList[i]).get().addOnSuccessListener {
                titleArrayList.add(it.child("title").value.toString())
                descriptionArrayList.add(it.child("title").value.toString())
                priorityArrayList.add(it.child("title").value.toString())
                timeArrayList.add(it.child("title").value.toString())
                dateArrayList.add(it.child("title").value.toString())
            }.addOnFailureListener {
                showToast("Failed")
            }

            i++
        }


        showDataOnListView()

        binding.btnAdd.setOnClickListener {
            val intent = Intent(applicationContext , AddTaskActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnReload.setOnClickListener {
            showDataOnListView()
//            val ctx = applicationContext
//            val pm = ctx.packageManager
//            val intent1 = pm.getLaunchIntentForPackage(ctx.packageName)
//            val mainIntent = Intent.makeRestartActivityTask(intent1?.component)
//            ctx.startActivity(mainIntent)
//            Runtime.getRuntime().exit(0)
        }


    }

    private fun showDataOnListView() {


        val taskList = readIdsFromFile(this , "all_ids.txt")
        val listView = binding.listView

        val adapterForMyListView = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        listView.adapter = adapterForMyListView

        listView.setOnItemClickListener { parent, view, position, id ->

            val text = "Clicked on item : " + (view as TextView).text.toString()
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }

    }

    private fun showToast(string: String?) {
        Toast.makeText(this , string , Toast.LENGTH_SHORT).show()
    }

    private fun getUsername(): String {
        var username : String = "Failed"
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
            return username
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Failed")
        }
        return username
    }



    // Function to write IDs to a file
    fun writeIdsToFile(context: Context, filename: String, ids: ArrayList<String>) {
        try {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                for (id in ids) {
                    outputStream.write(id.toByteArray())
                    outputStream.write("\n".toByteArray()) // Add a newline after each ID
                }
            }
            println("IDs successfully written to $filename")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Function to read IDs from a file
    fun readIdsFromFile(context: Context, filename: String): ArrayList<String> {
        val ids = ArrayList<String>()
        try {
            context.openFileInput(filename).use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    ids.add(line.orEmpty())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ids
    }

    // Function to update IDs in the file
    fun updateIdsInFile(context: Context, filename: String, newIds: ArrayList<String>) {
        val existingIds = readIdsFromFile(context, filename)
        existingIds.addAll(newIds)
        writeIdsToFile(context, filename, existingIds)
    }


    private fun <T> customLen(list: List<T>): Int {
        var count = 0
        for (item in list) {
            count++
        }
        return count
    }


}