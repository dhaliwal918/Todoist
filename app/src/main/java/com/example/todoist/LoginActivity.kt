package com.example.todoist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.MainActivities.MainListActivity
import com.example.todoist.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import java.io.FileOutputStream
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()

        binding.btnNext.setOnClickListener {
            if(binding.editTextUsername.text.isNullOrEmpty()||binding.editTextPassword.text.isNullOrEmpty()){
                Toast.makeText(this , "Fill all fields" , Toast.LENGTH_SHORT ).show()
            }else {
                FindUserInDatabase(username , password)
            }
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this , SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun FindUserInDatabase(username: String, password: String) {
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(binding.editTextUsername.text.toString()).get().addOnSuccessListener {
            if(it.exists()){
                val usernameFD = it.child("username").value.toString()
                val passwordFD = it.child("password").value.toString()

                if(passwordFD == binding.editTextPassword.text.toString()){
                    val fileName = "username.txt"
                    try {
                        val fileOutputStream = openFileOutput(fileName , MODE_PRIVATE)
                        fileOutputStream.write(usernameFD.toByteArray())
                        fileOutputStream.close()
                    }catch (e : IOException) {
                        e.printStackTrace()
                        showToast("Failed")
                    }
                    val intent = Intent(this , MainListActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    showToast("Incorrect password")
                    binding.editTextPassword.text.clear()
                }
            }else {
                UsernameNotExistDialog()
            }
        }.addOnFailureListener {
            showToast("Failed")
        }
    }

    public fun showToast(string: String) {
        Toast.makeText(this , string , Toast.LENGTH_SHORT ).show()
    }

    private fun UsernameNotExistDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.baseline_browser_not_supported_24)
        builder.setTitle("Username not Exist")
        builder.setPositiveButton("OK" , DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }

}

