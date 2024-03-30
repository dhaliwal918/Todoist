package com.example.todoist.AddTask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.MainActivities.MainListActivity
import com.example.todoist.R
import com.example.todoist.TaskData
import com.example.todoist.databinding.ActivityAddTaskBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddTaskBinding
    private lateinit var database : DatabaseReference
    private lateinit var username : String
    private lateinit var priority : String
    private lateinit var arrayList : ArrayList<String>
    private var filename : String = "all_ids.txt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
            binding.btnAdd.setOnClickListener {
                if(binding.editTextTitle.text.isNullOrEmpty()||binding.editTextDescription.text.isNullOrEmpty()||
                    binding.editTextDate.text.isNullOrEmpty()||binding.editTextTime.text.isNullOrEmpty()) {
                    showToast("fill empty fields")
                }else {
                    if(binding.MedRB.isChecked||binding.lowRB.isChecked||binding.highRB.isChecked){
                        if(binding.highRB.isChecked){
                            priority = "high"
                        }else if(binding.MedRB.isChecked){
                            priority = "medium"
                        }else
                            priority = "low"
                        AddTaskToDatabase()
                    }else {
                        showToast("set Priority First")
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Failed")
        }
        binding.backButton.setOnClickListener {
            val intent = Intent(applicationContext , MainListActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun AddTaskToDatabase() {
        database = FirebaseDatabase.getInstance().getReference("tasks")
        val bool = checkIdExistOrNot()
        if(bool==true){
            val taskData = TaskData(binding.editTextTitle.text.toString(), binding.editTextDescription.text.toString() ,
                binding.editTextDate.text.toString() , binding.editTextTime.text.toString() , priority )
            database.child(username).child(binding.editTextID.text.toString()).setValue(taskData).addOnSuccessListener {
                arrayList = readIdsFromFile(this , filename)
                arrayList.add(binding.editTextID.text.toString())
                writeIdsToFile(this , filename , arrayList)
                val intent = Intent(applicationContext , MainListActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                showToast("failed")
            }
        }else {
            showToast("id Should be unique")
        }
    }

    private fun checkIdExistOrNot(): Boolean {
        var ans : Boolean = false
        database.child(username).child(binding.editTextID.text.toString()).get().addOnSuccessListener {
            ans = it.exists()
        }.addOnFailureListener {
            showToast("failed")
            ans = false
        }
        return !ans
    }

    private fun showToast(string: String) {
        Toast.makeText(applicationContext , string , Toast.LENGTH_SHORT ).show()
    }


    // Function to write IDs to a file
    private fun writeIdsToFile(context: Context, filename: String, ids: ArrayList<String>) {
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
    private fun readIdsFromFile(context: Context, filename: String): ArrayList<String> {
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
    private fun updateIdsInFile(context: Context, filename: String, newIds: ArrayList<String>) {
        val existingIds = readIdsFromFile(context, filename)
        existingIds.addAll(newIds)
        writeIdsToFile(context, filename, existingIds)
    }

    // Example usage
//    val filename = "my_ids.txt"
//    val initialIds = arrayListOf("1", "2", "3")
//
//    // Write initial IDs to the file
//    writeIdsToFile(applicationContext, filename, initialIds)
//
//    // Read IDs from the file
//    val restoredIds = readIdsFromFile(applicationContext, filename)
//    println("Restored IDs: $restoredIds")
//
//    // Update IDs in the file
//    val newIdsToAdd = arrayListOf("4", "5", "6")
//    updateIdsInFile(applicationContext, filename, newIdsToAdd)
//
//    // Read the updated IDs
//    val updatedIds = readIdsFromFile(applicationContext, filename)
//    println("Updated IDs: $updatedIds")

}